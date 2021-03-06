package com.zen.capture.commons.domain.service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.zen.capture.commons.domain.models.ICapture;
import com.zen.capture.commons.domain.models.ICaptureRepository;
import com.zen.capture.commons.domain.models.ICaptureService;

public abstract class ACaptureService<T, U> implements ICaptureService<T, U> {

	public ICapture<U> getCapture(int index) {
		try {
			return getRepository().getCapture(index).get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public ICapture<T> getCapture(int index, int step) {
		try {
			return getRepository().getCapture(index, step).get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public int discover() {
		return getRepository().discover();
	}

	public Map<String, Object> getInfo(int id) {
		return getRepository().getInfo(id);
	}

	public List<Integer> getCaptureList() {
		return getRepository().getCaptureList();
	}

	public Map<String, Map<String, Object>> getInfo() {
		return getRepository().getInfo();
	}

	public boolean setProp(int id, String prop, String val) {
		return getRepository().setProp(id, prop, val);
	}

	protected abstract ICaptureRepository<T, U> getRepository();

	protected abstract String getAsString(T image);
}
