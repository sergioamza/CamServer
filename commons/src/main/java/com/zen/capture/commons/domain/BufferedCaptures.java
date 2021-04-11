package com.zen.capture.commons.domain;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.opencv.videoio.VideoCapture;

public class BufferedCaptures extends ACaptures<BufferedImage, BufferedImage> implements Serializable, ICaptures<BufferedImage, BufferedImage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BufferedCaptures(int index, int states) {
		super(index, states);
	}

	public BufferedCaptures(VideoCapture vCapture, int states) {
		super(vCapture, states);
	}

	@Override
	public ICapture<BufferedImage> getDefaulType() {
		return new BufferedImageCapture();
	}

	@Override
	public BufferedImage toBufferedImage(BufferedImage image) {
		return image;
	}

	@Override
	public ICapture<BufferedImage> getDefaultInstance() {
		return new BufferedImageCapture();
	}

}
