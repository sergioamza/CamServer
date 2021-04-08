package com.zen.cam.server.infraestructure.repository;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.zen.cam.commons.domain.ICamRepository;
import com.zen.cam.commons.domain.ImageUtils;

@Primary
@Repository
public class OCamRepository implements AutoCloseable, ICamRepository {

	private Logger logger = Logger.getLogger(OCamRepository.class.getName());

	private List<VideoCapture> webcam;

	private List<Long> lastImageTime = new ArrayList<>();

	private List<BufferedImage> images = new ArrayList<>();

	private OCamRepository() {
		init();
	}

	public Image getImage(int index) {
		long now = System.currentTimeMillis();
		while (images.size() <= index) {
			images.add(new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR));
		}
		while (lastImageTime.size() <= index) {
			lastImageTime.add(now);
		}
		try {
			Mat frame = new Mat();
			if ((now - lastImageTime.get(index)) >= 33 && webcam.get(index).read(frame)) {
				images.set(index, ImageUtils.getInstance().mat2WithContBufferedImage(frame));
			}
			if (images.get(index) == null) {
				throw new CvException("Null image");
			}
		} catch (IndexOutOfBoundsException | CvException e) {
			logger.severe(e.getMessage());
			Image im = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
			im.getGraphics().drawString(e.getMessage(), 0, 240);
			return im;
		}
		return images.get(index);
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
		webcam.forEach(VideoCapture::release);
	}

	public int discover() {
		Mat frame = new Mat();
		if (webcam != null && !webcam.isEmpty()) {
			webcam.forEach(VideoCapture::release);
		}
		webcam = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			VideoCapture camera = new VideoCapture(i);
			if (camera.isOpened() && camera.read(frame) && frame.width() > 0 && frame.height() > 0) {
				webcam.add(camera);
			} else {
				camera.release();
			}
		}
		return webcam.size();
	}

	public String getInfo(int index) {
		return webcam.get(index).getBackendName() + " " + webcam.get(index).getExceptionMode() + "@"
				+ webcam.get(index).getNativeObjAddr();
	}

	public String getImageAsString(int index) {
		return ImageUtils.getInstance().getImageAsString((BufferedImage) images.get(index));
	}
}
