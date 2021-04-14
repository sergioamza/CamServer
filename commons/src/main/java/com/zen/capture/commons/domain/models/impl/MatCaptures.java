package com.zen.capture.commons.domain.models.impl;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.zen.capture.commons.domain.models.ACaptures;
import com.zen.capture.commons.domain.models.ICapture;
import com.zen.capture.commons.domain.models.ICaptureDevice;
import com.zen.capture.commons.utils.ImageUtils;

public class MatCaptures extends ACaptures<VideoCapture, Mat, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	{
		nu.pattern.OpenCV.loadShared();
	}
	
	public MatCaptures(int index, int states) {
		super(index, states);
	}

	public MatCaptures(ICaptureDevice<VideoCapture, Mat> vCapture, int states) {
		super(vCapture, states);
	}

	@Override
	public ICapture<Mat> getTTypeDefault() {
		return new MatCapture();
	}

	@Override
	protected String fromTtoU(Mat image) {
		return (image != null && image.width() > 0 && image.height() > 0) ?  
				ImageUtils.getInstance().getImageAsString(ImageUtils.getInstance().matToBufferedImage(image)) : null;
	}

	@Override
	protected ICapture<String> getUTypeDefault() {
		return new StringCapture();
	}

}
