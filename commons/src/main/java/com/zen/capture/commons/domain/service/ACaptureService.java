package com.zen.capture.commons.domain.service;

import java.util.List;
import java.util.Map;

import com.zen.capture.commons.domain.ICapture;
import com.zen.capture.commons.domain.ICaptureRepository;
import com.zen.capture.commons.domain.ICaptureService;
import com.zen.capture.commons.domain.StringCapture;

public abstract class ACaptureService<T> implements ICaptureService<T> {

	public ICapture<T> getCapture(int index) {
		return getRepository().getCapture(index).get();
	}

	public ICapture<String> getCaptureString(int index) {
		ICapture<String> capture = new StringCapture();
		getRepository().getCapture(index).ifPresent(x -> {
			capture.setImage(getAsString(x.getImage()));
			capture.setCaptureTime(x.getCaptureTime());
		});
		return capture;
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

	protected abstract ICaptureRepository<T> getRepository();

	protected abstract String getAsString(T image);
}
