package com.zen.capture.commons.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICaptureRepository<T> {

	public Optional<ICapture<T>> getCapture(int index);
	public int discover();
	public Map<String, Object> getInfo(int index);
	public List<Integer> getCaptureList();
	public Map<String, Map<String, Object>> getInfo();
	boolean setProp(int idx, String prop, String value);
}
