package com.zen.cam.commons.domain;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageUtils {
	
	private static ImageUtils instance = null;
	
	private ImageUtils()	{
		super();
	}
	
	public static ImageUtils getInstance()	{
		if(instance == null)	{
			instance = new ImageUtils();
		}
		return instance;
	}

	public BufferedImage mat2bufferedImage(Mat in) {
		return mat2bufferedImage(in, BufferedImage.TYPE_3BYTE_BGR);
	}
	
	public BufferedImage mat2bufferedImage(Mat in, int bIType) {
		BufferedImage out = new BufferedImage(in.width(), in.height(), bIType);
		byte[] data = ((DataBufferByte) out.getRaster().getDataBuffer()).getData();
		in.get(0, 0, data);
		return out;
	}

	public BufferedImage mat2bufferedImageGray(Mat in) {
		return mat2bufferedImage(mat2GrayMat(in), BufferedImage.TYPE_BYTE_GRAY);
	}

	public BufferedImage mat2ThresBufferedImage(Mat in)	{
		return mat2bufferedImage(mat2ThresMat(mat2GrayMat(in)), BufferedImage.TYPE_BYTE_GRAY);
	}
	
	public Mat mat2GrayMat(Mat in) {
		Mat outerBox = new Mat(in.size(), CvType.CV_8UC1);
		Imgproc.cvtColor(in, outerBox, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(outerBox, outerBox, new Size(3, 3), 0);
		return outerBox;
	}

	public Mat mat2ThresMat(Mat in) {
		Imgproc.adaptiveThreshold(in, in, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 7, 2);
		return in;
	}

	public Mat matDiff(Mat in, Mat sub) {
		Mat out = new Mat();
		Core.subtract(in, in, out);
		return out;
	}
	
	public String getImageAsString(BufferedImage image) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ImageIO.write(image, "jpg", bos);
			return Base64.getMimeEncoder().encodeToString(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
