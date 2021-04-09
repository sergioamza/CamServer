package com.zen.capture.commons.domain;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageUtils {

	private static Logger logger = Logger.getLogger(ImageUtils.class.getName());
	
	private static ImageUtils instance = null;

	private ImageUtils() {
		super();
	}

	public static ImageUtils getInstance() {
		if (instance == null) {
			instance = new ImageUtils();
		}
		return instance;
	}

	public BufferedImage bufferedImage2bGrayBufferedImage(BufferedImage in) {
		return mat2GrayBufferedImage(bufferedImage2Mat(in, CvType.CV_8UC3));
	}

	public Mat bufferedImage2Mat(BufferedImage in, int cvType) {
		byte[] pixels = ((DataBufferByte) in.getRaster().getDataBuffer()).getData();
		Mat out = new Mat(in.getHeight(), in.getWidth(), cvType);
		out.put(0, 0, pixels);
		return out;
	}

	public BufferedImage mat2BufferedImage(Mat in) {
		return mat2BufferedImage(in, BufferedImage.TYPE_3BYTE_BGR);
	}

	public BufferedImage mat2BufferedImage(Mat in, int biType) {
		BufferedImage out = new BufferedImage(in.width(), in.height(), biType);
		byte[] data = ((DataBufferByte) out.getRaster().getDataBuffer()).getData();
		in.get(0, 0, data);
		return out;
	}

	public BufferedImage mat2BufferedImageWithCont(Mat in) {
		int biType = BufferedImage.TYPE_BYTE_GRAY;
		if(in.type() == CvType.CV_8UC3)	{
			biType = BufferedImage.TYPE_3BYTE_BGR;
		}
		return mat2BufferedImage(drawContours(in,mat2ThresMat(mat2GrayMat(in))), biType);
	}

	public BufferedImage mat2GrayBufferedImage(Mat in) {
		return mat2BufferedImage(mat2GrayMat(in), BufferedImage.TYPE_BYTE_GRAY);
	}
	
	public BufferedImage mat2ThresBufferedImage(Mat in) {
		return mat2BufferedImage(mat2ThresMat(mat2GrayMat(in)), BufferedImage.TYPE_BYTE_GRAY);
	}

	public Mat mat2GrayMat(Mat in) {
		Mat proc = new Mat(in.size(), CvType.CV_8UC1);
		Imgproc.cvtColor(in, proc, Imgproc.COLOR_BGR2GRAY);
		return proc;
	}

	public Mat mat2ThresMat(Mat in) {
		Mat proc = new Mat(in.size(), CvType.CV_8UC1);
		Imgproc.GaussianBlur(in, proc, new Size(3, 3), 0);
		Imgproc.adaptiveThreshold(proc, proc, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 13, 5);
		return proc;
	}

	public Mat matDiff(Mat in, Mat sub) {
		Mat out = new Mat();
		Core.subtract(in, in, out);
		return out;
	}

	public String getImageAsString(BufferedImage image) {
		return getImageAsString(image, "jpg");
	}

	public String getImageAsString(BufferedImage image, String format) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ImageIO.write(image, format, bos);
			return Base64.getMimeEncoder().encodeToString(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<MatOfPoint> findContours(Mat image)	{
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		contours.removeIf((c) -> !c.isContinuous());
		return contours;
	}
	
	public Mat drawContours(Mat image,Mat contImage)	{
		Scalar color = new Scalar(255,0,0);
		Imgproc.drawContours(image, findContours(contImage), -1, color);
		return image;
	}
}
