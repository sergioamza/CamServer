package com.zen.capture.commons.domain.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.opencv.core.CvException;

public abstract class ACaptureRepository<D, T, U> implements AutoCloseable, ICaptureRepository<T,U> {

	private Logger logger = Logger.getLogger(ACaptureRepository.class.getName());

	private Map<Integer, ICaptures<D, T, U>> captures;

	private final int STATES = 3;

	private ACaptureRepository() {
		init();
	}

	public Optional<ICapture<U>> getCapture(int index) {
		long now = System.currentTimeMillis();
		try {
			if (!captures.containsKey(index))
				throw new NullPointerException("No such device");
			if ((now - captures.get(index).getCapture().getCaptureTime()) >= 50
					&& captures.get(index).getvCapture().isReady()) {
				captures.get(index).setCapture(captures.get(index).getvCapture().getCapture());
			}
			if (captures.get(index).getCapture().getImage() == null) {
				throw new CvException("Null image");
			}
		} catch (NullPointerException e) {
		} catch (IndexOutOfBoundsException | CvException e) {
		}
		return Optional.of(captures.get(index).getCapture());
	}

	private void init() {
		try {
			nu.pattern.OpenCV.loadShared();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public int discover() {
		if (captures != null && !captures.isEmpty()) {
			captures.forEach((i, cap) -> {
				try {
					cap.getvCapture().close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
					e.printStackTrace();
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
