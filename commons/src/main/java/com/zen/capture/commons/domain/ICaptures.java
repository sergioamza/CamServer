package com.zen.capture.commons.domain;

import org.opencv.videoio.VideoCapture;

public interface ICaptures<T, U> {
	
	public void setvCapture(VideoCapture vCapture);

	public ICapture<T> getCapture();

	public ICapture<T> getCapture(int state);

	public void setCapture(T image);

	public ICapture<U> getImageCapture();

	public ICapture<T> getDefaulType();

	public U toBufferedImage(T image);

	public VideoCapture getvCapture();
}
