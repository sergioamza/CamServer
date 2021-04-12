package com.zen.capture.commons.domain.models;

import java.util.Map;

public interface ICaptureDevice<D, T> extends AutoCloseable {

	public boolean isOpen();
	
	public boolean isReady();

	public void setCaptureDevice(D device);
	
	public D getCaptureDevice();

	public boolean open(int id);

	public T getCapture();

	public Map<String, Object> getProps();

	public boolean setProp(String prop, Object val);

	public ICaptureDevice<D, T> getDefaultDevice(String id);

	boolean open(String filename);


}
