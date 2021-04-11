package com.zen.capture.commons.domain;

import org.opencv.core.Mat;

public class MatCapture implements ICapture<Mat> {
	
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
