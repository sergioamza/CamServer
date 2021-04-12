package com.zen.capture.commons.domain.models;
/**
 * 
 * @author Sergio Martinez
 *
 * @param <D>	Device Type
 * @param <T>	Image store format for time images
 * @param <U>	Image store format for current image
 */
public interface ICaptures<D, T, U> {
	
	public void setvCapture(ICaptureDevice<D,T> vCapture);

	public ICapture<U> getCapture();

	public ICapture<T> getCapture(int state);

	public void setCapture(T image);

	public ICaptureDevice<D,T> getvCapture();
}
