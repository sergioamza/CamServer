package com.zen.capture.commons.domain;

import java.awt.image.BufferedImage;

public class BufferedImageCapture implements ICapture<BufferedImage> {

	private BufferedImage image;
	private long captureTime;

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public long getCaptureTime() {
		return captureTime;
	}

	public void setCaptureTime(long captureTime) {
		this.captureTime = captureTime;
	}

}
