package com.zen.capture.server.infraestructure.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.zen.capture.commons.domain.models.ACaptureRepository;
import com.zen.capture.commons.domain.models.ICaptureDevice;
import com.zen.capture.commons.domain.models.ICaptureRepository;
import com.zen.capture.commons.domain.models.ICaptures;
import com.zen.capture.commons.domain.models.impl.MatCaptures;
import com.zen.capture.commons.domain.models.impl.MatOpencvCaptureDevice;
import com.zen.capture.commons.utils.StringUtils;

@Primary
@Repository
public class CaptureRepository<T> extends ACaptureRepository<VideoCapture, Mat, String> implements AutoCloseable, ICaptureRepository<Mat, String> {

	private Logger logger = Logger.getLogger(CaptureRepository.class.getName());

	{
		nu.pattern.OpenCV.loadShared();
	}
	
	private Map<String, Integer> props = new HashMap<String, Integer>();
	
	public CaptureRepository()	{
		super();
		init();
	}
	
	public void updateCapture(int index) {
		long now = System.currentTimeMillis();
		if (captures.containsKey(index) && (now - captures.get(index).getCapture().getCaptureTime()) >= 100
				&& captures.get(index).getvCapture().isReady()) {
			captures.get(index).setCapture(captures.get(index).getvCapture().getCapture());
		}
	}

	private void init() {
		setProps();
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

	public Map<String, Object> getInfo(int index) {
		Map<String, Object> cProps = new HashMap<>();
		props.forEach((k, ve) -> {
			double va = captures.get(index).getvCapture().getCaptureDevice().get(ve);
			if (va != -1.0) {
				cProps.put(k, va);
			}
		});
		return cProps;
	}

	public boolean setProp(int idx, String prop, String value) {
		return captures.get(idx).getvCapture().getCaptureDevice().set((int) props.get(prop),
				Double.valueOf((String) value));
	}

	@Override
	public Map<String, Map<String, Object>> getInfo() {
		Map<String, Map<String, Object>> info = new HashMap<>();
		captures.forEach((idx, cap) -> {
			info.put("" + idx, getInfo(idx));
		});
		return info;
	}

	private void setProps() {
		try {
			Videoio v = Videoio.class.newInstance();
			List<Field> fields = Arrays.asList(v.getClass().getFields());
			fields.stream().filter(x -> {
				return x.getName().toLowerCase().trim().startsWith("CAP".toLowerCase());
			}).forEach((x) -> {
				try {
					props.put(StringUtils.getInstance().snakeCaseToUpperCamelCase(x.getName()), x.getInt(x));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					logger.info(e.getMessage());
				}
			});
		} catch (Exception e) {

		}
	}

	@Override
	public List<Integer> getCaptureList() {
		List<Integer> list = new ArrayList<>();
		captures.forEach((i, cap) -> {
			list.add(i);
		});
		return list;
	}

	@Override
	protected ICaptures<VideoCapture, Mat, String> getNewCapturesInstance(ICaptureDevice<VideoCapture, Mat> vCapture,
			int states) {
		return new MatCaptures(vCapture, states);
	}

	@Override
	protected ICaptureDevice<VideoCapture, Mat> getNewCaptureDeviceInstance(int id) {
		return new MatOpencvCaptureDevice(id);
	}

}
