package com.zen.capture.commons.domain.models.impl;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.zen.capture.commons.domain.models.ICaptureDevice;
import com.zen.capture.commons.utils.ImageUtils;

public class BufferedImageOpencvCaptureDevice extends OpencvCaptureDevice<BufferedImage> {

	private static Logger logger = Logger.getLogger(BufferedImageOpencvCaptureDevice.class.getName());

	public BufferedImageOpencvCaptureDevice(String id) {
		super(id);
	}

	public BufferedImageOpencvCaptureDevice(int id) {
		super(id);
	}

	@Override
	public ICaptureDevice<VideoCapture, BufferedImage> getDefaultDevice(String id) {
		return new BufferedImageOpencvCaptureDevice(id);
	}

	@Override
	protected BufferedImage convertMatToT(Mat image) {
		return ImageUtils.getInstance().mat2BufferedImage(image);
	}

}
