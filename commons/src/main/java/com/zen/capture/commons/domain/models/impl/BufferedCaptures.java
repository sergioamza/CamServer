package com.zen.capture.commons.domain.models.impl;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.opencv.videoio.VideoCapture;

import com.zen.capture.commons.domain.models.ACaptures;
import com.zen.capture.commons.domain.models.ICapture;
import com.zen.capture.commons.domain.models.ICaptureDevice;
import com.zen.capture.commons.domain.models.ICaptures;
import com.zen.capture.commons.utils.ImageUtils;

public class BufferedCaptures extends ACaptures<VideoCapture, BufferedImage, String> implements Serializable, ICaptures<VideoCapture,BufferedImage, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BufferedCaptures(int index, int states) {
		super(index, states);
	}

	public BufferedCaptures(ICaptureDevice<VideoCapture, BufferedImage> vCapture, int states) {
		super(vCapture, states);
	}
	
	@Override
	protected String fromTtoU(BufferedImage image) {
		return ImageUtils.getInstance().getImageAsString(image);
	}

	@Override
	protected ICapture<BufferedImage> getTTypeDefault() {
		return new BufferedImageCapture();
	}

	@Override
	protected ICapture<String> getUTypeDefault() {
		return new StringCapture();
	}

}
