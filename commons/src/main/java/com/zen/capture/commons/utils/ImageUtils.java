package com.zen.capture.commons.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageUtils {

	private static Logger logger = Logger.getLogger(ImageUtils.class.getName());

	{
		nu.pattern.OpenCV.loadShared();
	}
	
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

	public BufferedImage bufferedImageToGrayBufferedImage(BufferedImage in) {
		return mat2GrayBufferedImage(bufferedImageToMat(in, CvType.CV_8UC3));
	}

	public Mat bufferedImageToMat(BufferedImage in, int cvType) {
		byte[] pixels = ((DataBufferByte) in.getRaster().getDataBuffer()).getData();
		Mat out = new Mat(in.getHeight(), in.getWidth(), cvType);
		out.put(0, 0, pixels);
		return out;
	}
	

	public int matTypeTobiType(int matType)	{
		int biType = BufferedImage.TYPE_BYTE_GRAY;
		if (matType == CvType.CV_8UC2 || matType == CvType.CV_8SC2) {
			biType = BufferedImage.TYPE_3BYTE_BGR;
		} else if (matType == CvType.CV_8UC3 || matType == CvType.CV_8SC3)	{
			biType = BufferedImage.TYPE_3BYTE_BGR;			
		} else if (matType == CvType.CV_8UC4 || matType == CvType.CV_8SC4)	{
			biType = BufferedImage.TYPE_4BYTE_ABGR;			
		}
		return biType;
	}
	

	public int biTypeToMatType(int biType)	{
		int matType = CvType.CV_8U;
		/*if (biType == BufferedImage.TYPE_ || biType == BufferedImage.TYPE_4BYTE_ABGR_PRE)	{
			matType = CvType.CV_8UC4;			
		} else
		if (biType == BufferedImage.TYPE_3BYTE_BGR)	{
			matType = CvType.CV_8UC3;			
		} else if (biType == BufferedImage.TYPE_4BYTE_ABGR || biType == BufferedImage.TYPE_4BYTE_ABGR_PRE)	{
			matType = CvType.CV_8UC4;			
		}*/
		return matType;
	}


	public BufferedImage matToBufferedImage(Mat in) {
		return matToBufferedImage(in, matTypeTobiType(in.type()));
	}

	public BufferedImage matToBufferedImage(Mat in, int biType) {
		BufferedImage out = new BufferedImage(in.width(), in.height(), biType);
		byte[] data = ((DataBufferByte) out.getRaster().getDataBuffer()).getData();
		in.get(0, 0, data);
		return out;
	}

	public BufferedImage mat2BufferedImageWithCont(Mat in) {
		return matToBufferedImage(drawContours(in, mat2ThresMat(mat2GrayMat(in))), matTypeTobiType(in.type()));
	}

	public BufferedImage mat2GrayBufferedImage(Mat in) {
		return matToBufferedImage(mat2GrayMat(in), BufferedImage.TYPE_BYTE_GRAY);
	}

	public BufferedImage mat2ThresBufferedImage(Mat in) {
		return matToBufferedImage(mat2ThresMat(mat2GrayMat(in)), BufferedImage.TYPE_BYTE_GRAY);
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
	
	public Mat matResize(Mat in, int cols, int rows)	{
		Size s = new Size(new double[] {cols, rows});
		Mat out = new Mat(in.size(),in.type());
		Imgproc.resize(in, out, s);
		return out;
	}

	public Mat matDiff(Mat in, Mat sub) {
		Mat out = new Mat();
		Core.subtract(in, sub, out);
		return out;
	}

	public String getImageAsString(BufferedImage image) {
		return getImageAsString(image, "jpg");
	}

	public String getImageAsString(BufferedImage image, String format) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ImageIO.write(image, format, bos);
			return Base64.getMimeEncoder().encodeToString(bos.toByteArray());
		} catch (IOException | IllegalArgumentException e) {
			logger.warning(e.getMessage());
		}
		return null;
	}
	
	public BufferedImage getBufferedImage(String in)	{
		byte bt[] = Base64.getMimeDecoder().decode(in);
		InputStream is = new ByteArrayInputStream(bt);		
		try {
			return ImageIO.read(is);
		} catch (IOException e) {
			logger.warning(e.getMessage());
		}
		return null;
	}

	public List<MatOfPoint> findContours(Mat image) {
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		contours.removeIf((c) -> !c.isContinuous());
		return contours;
	}

	public Mat drawContours(Mat image, Mat contImage) {
		Scalar color = new Scalar(255, 0, 0);
		Imgproc.drawContours(image, findContours(contImage), -1, color);
		return image;
	}

	public Mat drawContoursAndFilter(Mat image, Mat contImage) {
		Scalar color = new Scalar(255, 0, 0);
		List<MatOfPoint> points = findContours(contImage);
		if (points != null)	{	
			getBoxesAndFilterPoints(points);
			Imgproc.drawContours(image, points, -1, color);
		}
		return image;
	}
	
	public Image getMessageImage(String message, int width, int height)	{
		Image im = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		im.getGraphics().drawString(message, 0, width/2);
		return im;
	}
	
	public byte[] mat2ByteArray(Mat in)	{
		byte[] raw_data = new byte[(int) (in.total() * in.channels())];
        in.put(0, 0, raw_data);
        return raw_data;
	}
	
	public Mat byteArray2Mat(byte[] in, int cols, int rows, int cvType)	{
		Mat out = new Mat(rows, cols, cvType);
		out.get(rows, cols, in);
		return out;
	}
	
	public String byteArray2Base64String(byte[] in)	{
		return Base64.getMimeEncoder().encodeToString(in);
	}
	
	public byte[] base64String2byteArray(String in)	{
		return Base64.getMimeDecoder().decode(in);
	}
	
	public String matToBase64String(Mat in)	{
		return byteArray2Base64String(mat2ByteArray(in));
	}
	
	public Mat base64StringToMat(String in, int cols, int rows, int cvType)	{
		return byteArray2Mat(base64String2byteArray(in), rows, cols, cvType);
	}
	
	public Mat filter2D(Mat src)	{
		Mat gray = mat2GrayMat(src);
		Mat kernel = new Mat(3, 3, src.type());
		kernel.put(0, 0, /* R */1, 0, 1);
		kernel.put(0, 0, /* R */0, 1, 0);
		kernel.put(0, 0, /* R */1, 0, 1);
		///*kernel.put(1, 0, /* G */0.168f, 0.686f, 0.349f, 0f);
		//kernel.put(2, 0, /* B */0.131f, 0.534f, 0.272f, 0f);
		//kernel.put(3, 0, /* A */0.000f, 0.000f, 0.000f, 1f);*/
		Mat dst = new Mat(gray.rows(), gray.cols(), -1);
		Imgproc.filter2D(gray, dst, 1, kernel);
		//Imgproc.sepFilter2D(src, dst, 2, kernel, kernel);
		return mat2GrayMat(dst);
	}

	public List<RotatedRect> getBoxesAndFilterPoints(List<MatOfPoint> points)	{
		List<RotatedRect> boxes = new ArrayList<RotatedRect>();
		points.forEach((p) -> {
			RotatedRect box = new RotatedRect();
			Imgproc.boxPoints(box, p);
			if (box.size.height > 30 && box.size.width > 30) boxes.add(box);
			else points.remove(p);
		});
		return boxes;
	}
}
