package com.zen.capture.commons.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.opencv.videoio.VideoCapture;

public abstract class ACaptures<T,U> implements Serializable, ICaptures<T,U> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ACaptures.class.getName());

	private VideoCapture vCapture;
	private ICapture<U> image;
	private Map<Integer, ICapture<T>> captures;
	private int states;

	public ACaptures(int index, int states) {
		vCapture = new VideoCapture(index);
		captures = new HashMap<Integer, ICapture<T>>(states);
		this.states = states;
		this.image = getDefaultInstance();
	}

	public ACaptures(VideoCapture vCapture, int states) {
		this.vCapture = vCapture;
		captures = new HashMap<Integer, ICapture<T>>(states);
		this.states = states;
		this.image = getDefaultInstance();
	}

	public VideoCapture getvCapture() {
		return vCapture;
	}

	public void setvCapture(VideoCapture vCapture) {
		this.vCapture = vCapture;
	}

	public ICapture<T> getCapture() {
		return getCapture(0);
	}

	public ICapture<T> getCapture(int state) {
		for (int i = 0; i < states; i++) {
			try {
				if (captures.get(i) == null)
					throw new NullPointerException("Uninitialized");
			} catch (NullPointerException e) {
				captures.put(i, getDefaulType());
				logger.warning(e.getMessage());
				logger.fine("Capture: " + i);
			}			
		}
		return captures.get(state);
	}

	public void setCapture(T image) {
		long now = System.currentTimeMillis();
		for (int i = 0; i < states; i++) {
			if (i > 0) {
				logger.fine("Copy from  " + i + " to " + (i + 1));
				getCapture(i - 1).copyTo(getCapture(i));
			}
		}
		getCapture(0).setImage(image);
		getCapture(0).setCaptureTime(now);
		this.image.setCaptureTime(now);
		this.image.setImage(toBufferedImage(image));
	}
	
	public ICapture<U> getImageCapture()	{
		return this.image;
	}
	
	public abstract ICapture<U> getDefaultInstance();
}
