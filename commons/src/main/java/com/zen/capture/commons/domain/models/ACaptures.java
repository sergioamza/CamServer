package com.zen.capture.commons.domain.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class ACaptures<D, T, U> implements Serializable, ICaptures<D, T, U> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ACaptures.class.getName());

	private ICaptureDevice<D, T> captureDevice;
	private ICapture<U> image;
	private Map<Integer, ICapture<T>> captures;
	private int states;

	public ACaptures(int index, int states) {
		captureDevice = captureDevice.getDefaultDevice("" + index);
		captures = new HashMap<Integer, ICapture<T>>(states);
		this.states = states;
		this.image = getUTypeDefault();
	}

	public ACaptures(ICaptureDevice<D, T> vCapture, int states) {
		this.captureDevice = vCapture;
		captures = new HashMap<Integer, ICapture<T>>(states);
		this.states = states;
		this.image = getUTypeDefault();
	}

	public ICaptureDevice<D, T> getvCapture() {
		return captureDevice;
	}

	public void setvCapture(ICaptureDevice<D, T> vCapture) {
		this.captureDevice = vCapture;
	}

	public ICapture<U> getCapture() {
		return image;
	}

	public ICapture<T> getCapture(int state) {
		for (int i = 0; i < states; i++) {
			try {
				if (captures.get(i) == null)
					throw new NullPointerException("Uninitialized");
			} catch (NullPointerException e) {
				captures.put(i, getTTypeDefault());
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
		this.image.setImage(fromTtoU(image));
	}
	
	protected abstract U fromTtoU(T image);

	protected abstract ICapture<T> getTTypeDefault();

	protected abstract ICapture<U> getUTypeDefault();

}
