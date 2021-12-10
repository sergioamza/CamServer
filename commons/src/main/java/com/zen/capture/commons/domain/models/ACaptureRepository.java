package com.zen.capture.commons.domain.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.opencv.core.CvException;

public abstract class ACaptureRepository<D, T, U> implements AutoCloseable, ICaptureRepository<T, U> {

	private Logger logger = Logger.getLogger(ACaptureRepository.class.getName());

	{
		nu.pattern.OpenCV.loadShared();
	}

	protected Map<Integer, ICaptures<D, T, U>> captures = new HashMap<Integer, ICaptures<D, T, U>>();

	private final int STATES = 2;

	protected ACaptureRepository() {
		init();
	}

	@Override
	public Optional<ICapture<U>> getCapture(int index) {
		updateCapture(index);
		return (captures != null && captures.containsKey(index)) ? Optional.of(captures.get(index).getCapture()) : Optional.empty();
	}

	@Override
	public Optional<ICapture<T>> getCapture(int index, int state) {
		updateCapture(index);
		return (captures != null && captures.containsKey(index)) ? Optional.of(captures.get(index).getCapture(state))
				: Optional.empty();
	}

	@Override
	public void updateCapture(int index) {
		long now = System.currentTimeMillis();
		if (captures != null && (now - captures.get(index).getCapture().getCaptureTime()) >= 50
				&& captures.get(index).getvCapture().isReady()) {
			captures.get(index).setCapture(captures.get(index).getvCapture().getCapture());
		}
	}

	private void init() {
		try {
			discover();
		} catch (CvException e) {
			logger.severe(e.getMessage());
		}
	}

	@Override
	public void close() throws Exception {
		captures.forEach((i, cap) -> {
			try {
				cap.getvCapture().close();
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		});
	}

	public int discover() {
		if (captures != null && !captures.isEmpty()) {
			captures.forEach((i, cap) -> {
				try {
					if(cap.getvCapture().isOpen()) cap.getvCapture().close();
				} catch (Exception e) {
					logger.warning(e.getMessage());
				}
			});
		} else {
			captures = new HashMap<>();
		}

		for (int i = 0; i < 30; i++) {
			ICaptureDevice<D, T> vCapture = getNewCaptureDeviceInstance(i);
			if ((vCapture.isOpen() || vCapture.open(i)) && vCapture.isReady()) {
				captures.put(i, getNewCapturesInstance(vCapture, STATES));
				logger.info("Cam " + i + " " + vCapture.getProps());
			} else {
				try {
					vCapture.close();
				} catch (Exception e) {
					logger.warning(e.getMessage());
				}
				captures.remove(i);
			}
		}
		return captures.size();
	}

	@Override
	public Map<String, Map<String, Object>> getInfo() {
		Map<String, Map<String, Object>> info = new HashMap<>();
		captures.forEach((idx, cap) -> {
			info.put("" + idx, getInfo(idx));
		});
		return info;
	}

	@Override
	public List<Integer> getCaptureList() {
		List<Integer> list = new ArrayList<>();
		captures.forEach((i, cap) -> {
			list.add(i);
		});
		return list;
	}

	protected abstract ICaptures<D, T, U> getNewCapturesInstance(ICaptureDevice<D, T> vCapture, int states);

	protected abstract ICaptureDevice<D, T> getNewCaptureDeviceInstance(int id);
}
