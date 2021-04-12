package com.zen.capture.commons.domain.models.impl;

import java.util.logging.Logger;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.zen.capture.commons.domain.models.ICaptureDevice;

public class MatOpencvCaptureDevice extends OpencvCaptureDevice<Mat> {

	private static Logger logger = Logger.getLogger(MatOpencvCaptureDevice.class.getName());

	public MatOpencvCaptureDevice(String id) {
		super(id);
	}

	public MatOpencvCaptureDevice(int id) {
		super(id);
	}

	@Override
	public ICaptureDevice<VideoCapture, Mat> getDefaultDevice(String id) {
		return new MatOpencvCaptureDevice(id);
	}

	@Override
	protected Mat convertMatToT(Mat image) {
		return (image);
	}

}
