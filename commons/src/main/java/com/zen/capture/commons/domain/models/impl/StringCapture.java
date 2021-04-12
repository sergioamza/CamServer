package com.zen.capture.commons.domain.models.impl;

import java.awt.image.BufferedImage;

import com.zen.capture.commons.domain.models.ICapture;
import com.zen.capture.commons.utils.ImageUtils;

public class StringCapture implements ICapture<String> {

	private String image;
	private long captureTime;

	public String getImage() {
		return image;
	}

	public void setImage(BufferedImage image)	{
		this.image = ImageUtils.getInstance().getImageAsString(image);
	}
	
	public void setImage(String image) {
		this.image = image;
	}

	public long getCaptureTime() {
		return captureTime;
	}

	public void setCaptureTime(long captureTime) {
		this.captureTime = captureTime;
	}

}
