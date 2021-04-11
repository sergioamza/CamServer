package com.zen.capture.commons.domain;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class MatCaptures extends ACaptures<Mat, Mat> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MatCaptures(int index, int states) {
		super(index, states);
	}

	public MatCaptures(VideoCapture capture, int states) {
		super(capture, states);
	}

	@Override
	public ICapture<Mat> getDefaulType() {
		return new MatCapture();
	}

	@Override
	public Mat toBufferedImage(Mat image) {
		return image;
	}

	@Override
	public ICapture<Mat> getDefaultInstance() {
		return new MatCapture();
	}
}
