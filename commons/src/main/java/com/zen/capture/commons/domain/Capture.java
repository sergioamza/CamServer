package com.zen.capture.commons.domain;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.opencv.videoio.VideoCapture;

public class Capture implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private VideoCapture vCapture;
	private BufferedImage image;
	private BufferedImage lastImage;
	private long lastCaptureTime;
	private long currentCaptureTime;

	public Capture() {
		super();
		vCapture = new VideoCapture();
	}

	public Capture(int index) {
		vCapture = new VideoCapture(index);
	}
	
	public Capture(VideoCapture vCapture)	{
		this.vCapture = vCapture;
	}
	
	public VideoCapture getvCapture() {
		return vCapture;
	}

	public void setvCapture(VideoCapture vCapture) {
		this.vCapture = vCapture;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getLastImage() {
		return lastImage;
	}

	public void setLastImage(BufferedImage lastImage) {
		this.lastImage = lastImage;
	}

	public long getLastCaptureTime() {
		return lastCaptureTime;
	}

	public void setLastCaptureTime(long lastCaptureTime) {
		this.lastCaptureTime = lastCaptureTime;
	}

	public long getCurrentCaptureTime() {
		return currentCaptureTime;
	}

	public void setCurrentCaptureTime(long currentCaptureTime) {
		this.currentCaptureTime = currentCaptureTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
