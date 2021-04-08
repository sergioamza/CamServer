package com.zen.capture.server.infraestructure.repository;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.zen.capture.commons.domain.Capture;
import com.zen.capture.commons.domain.ICaptureRepository;
import com.zen.capture.commons.domain.ImageUtils;

@Primary
@Repository
public class CaptureRepository implements AutoCloseable, ICaptureRepository {

	private Logger logger = Logger.getLogger(CaptureRepository.class.getName());

	private Map<Integer, Capture> vCaptures;

	private CaptureRepository() {
		init();
	}

	public Image getImage(int index) {
		Capture vCapture = vCaptures.containsKey(index) ? vCaptures.get(index): new Capture(index);
		long now = System.currentTimeMillis();
		try {
			Mat image = new Mat();
			if ((now - vCapture.getLastCaptureTime()) >= 50 && vCapture.getvCapture().read(image)) {
				vCapture.setLastCaptureTime(now);
				vCapture.setLastImage(vCapture.getImage());
				vCapture.setImage(ImageUtils.getInstance().mat2BufferedImage(image));
			}
			if (vCapture.getImage() == null) {
				throw new CvException("Null image");
			}
		} catch (IndexOutOfBoundsException | CvException e) {
			logger.severe(e.getMessage());
			Image im = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
			im.getGraphics().drawString(e.getMessage(), 0, 240);
			return im;
		}
		return vCapture.getImage();
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
		vCaptures.forEach((i,cap) -> cap.getvCapture().release());
	}

	public int discover() {
		Mat frame = new Mat();
		if (vCaptures != null && !vCaptures.isEmpty()) {
			vCaptures.forEach((i,cap) -> cap.getvCapture().release());
		}	else	{
			vCaptures = new HashMap<>();			
		}
		for (int i = 0; i < 30; i++) {
			VideoCapture vCapture = new VideoCapture(i);
			if (vCapture.isOpened() && vCapture.read(frame) && frame.width() > 0 && frame.height() > 0) {
				vCaptures.put(i, new Capture(vCapture));
			} else {
				vCapture.release();
				vCaptures.remove(i);
			}
		}
		/*vCaptures.forEach(c -> c.setExceptionMode(true));
		vCaptures.forEach(c -> logger.info("Brightness " + c.get(Videoio.CAP_PROP_BRIGHTNESS)));
		vCaptures.forEach(c -> logger.info("WhiteBalance " + c.get(Videoio.CAP_PROP_AUTO_WB)));
		vCaptures.forEach(c -> logger.info("Exposure " + c.get(Videoio.CAP_PROP_EXPOSURE)));
		vCaptures.forEach(c -> logger.info("Gain " + c.get(Videoio.CAP_PROP_GAIN)));
		vCaptures.forEach(c -> logger.info("Contrast " + c.get(Videoio.CAP_PROP_CONTRAST)));
		vCaptures.forEach(c -> logger.info("Contrast " + c.set(Videoio.CAP_PROP_CONTRAST, 100.0)));
		vCaptures.forEach(c -> logger.info("Contrast " + c.get(Videoio.CAP_PROP_CONTRAST)));*/
		return vCaptures.size();
	}

	public String getInfo(int index) {
		return "";/*vCaptures.get(index).getBackendName() + " " + vCaptures.get(index).getExceptionMode() + "@"
				+ vCaptures.get(index).getNativeObjAddr();*/
	}

	public String getImageAsString(int index) {
		return ImageUtils.getInstance().getImageAsString((BufferedImage) getImage(index));
	}
}
