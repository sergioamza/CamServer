package com.zen.capture.commons.domain;

public interface ICapture<T> {

	public T getImage();

	public void setImage(T image);

	public long getCaptureTime();

	public void setCaptureTime(long currentCaptureTime);
	
	public default void copyTo(ICapture<T> dst)	{
		dst.setImage(getImage());
		dst.setCaptureTime(getCaptureTime());
	}
	
}
