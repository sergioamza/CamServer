package com.zen.capture.commons.domain.models.impl;

import org.opencv.core.Mat;

import com.zen.capture.commons.domain.models.ICapture;

public class MatCapture implements ICapture<Mat> {

	{
		nu.pattern.OpenCV.loadShared();
	}
	
	private Mat image;
	private long captureTime;

	public Mat getImage() {
		return image;
	}

	public void setImage(Mat image) {
		this.image = image;
	}

	public long getCaptureTime() {
		return captureTime;
	}

	public void setCaptureTime(long captureTime) {
		this.captureTime = captureTime;
	}

}
