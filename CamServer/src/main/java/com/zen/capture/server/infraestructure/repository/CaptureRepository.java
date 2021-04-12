package com.zen.capture.server.infraestructure.repository;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.zen.capture.commons.domain.models.ICapture;
import com.zen.capture.commons.domain.models.ICaptureDevice;
import com.zen.capture.commons.domain.models.ICaptureRepository;
import com.zen.capture.commons.domain.models.impl.MatCaptures;
import com.zen.capture.commons.domain.models.impl.MatOpencvCaptureDevice;
import com.zen.capture.commons.domain.models.impl.StringCapture;
import com.zen.capture.commons.utils.ImageUtils;
import com.zen.capture.commons.utils.StringUtils;

@Primary
@Repository
public class CaptureRepository<T> implements AutoCloseable, ICaptureRepository<Mat, String> {

	private Logger logger = Logger.getLogger(CaptureRepository.class.getName());

	private Map<Integer, MatCaptures> captures;

	private Map<String, Integer> props = new HashMap<String, Integer>();

	private final int STATES = 3;

	private CaptureRepository() {
		init();
	}

	public Optional<ICapture<String>> getCapture(int index) {
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
			Image im = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
			im.getGraphics().drawString(e.getMessage(), 0, 240);
			ICapture<String> capture = new StringCapture();
			capture.setCaptureTime(now);
			capture.setImage(ImageUtils.getInstance().getImageAsString((BufferedImage) im));
			return Optional.of(capture);
		} catch (IndexOutOfBoundsException | CvException e) {
			logger.warning("TimeElapsed: " + (now - captures.get(index).getCapture().getCaptureTime()));
			logger.warning(e.getMessage());
			logger.info("Intance [" + index + "] " + captures.get(index).getvCapture().hashCode());
			Image im = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
			im.getGraphics().drawString(e.getMessage(), 0, 240);
			captures.get(index).getCapture().setImage(ImageUtils.getInstance().getImageAsString((BufferedImage) im));
		}
		return Optional.of(captures.get(index).getCapture());
	}

	private void init() {
		try {
			nu.pattern.OpenCV.loadShared();
			setProps();
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
		Mat frame = new Mat();
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
			ICaptureDevice<VideoCapture, Mat> vCapture = new MatOpencvCaptureDevice(i);
			if (vCapture.isOpen()) {
				vCapture.getCaptureDevice().release();
			}
			if ((vCapture.getCaptureDevice().isOpened() || vCapture.getCaptureDevice().open(i))
					&& vCapture.getCaptureDevice().read(frame) && frame.width() > 0 && frame.height() > 0) {
				captures.put(i, new MatCaptures(vCapture, STATES));
				logger.info("Cam " + i + " " + vCapture.getCaptureDevice().get(Videoio.CAP_PROP_FRAME_WIDTH) + "x"
						+ vCapture.getCaptureDevice().get(Videoio.CAP_PROP_FRAME_HEIGHT) + "@"
						+ vCapture.getCaptureDevice().get(Videoio.CAP_PROP_FPS));
			} else {
				vCapture.getCaptureDevice().release();
				captures.remove(i);
			}
		}
		return captures.size();
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
					e.printStackTrace();
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
}
