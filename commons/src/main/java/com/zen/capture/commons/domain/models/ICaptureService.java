package com.zen.capture.commons.domain.models;

import java.util.List;
import java.util.Map;

public interface ICaptureService<T, U> {

	public ICapture<U> getCapture(int index);

	public ICapture<T> getCapture(int index, int step);

	public int discover();

	public Map<String, Object> getInfo(int id);

	public Map<String, Map<String, Object>> getInfo();

	public List<Integer> getCaptureList();

	public boolean setProp(int id, String prop, String val);

}
