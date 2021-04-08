package com.zen.cam.server.infraestructure.repository;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.zen.cam.commons.domain.ICamRepository;
import com.zen.cam.commons.domain.ImageUtils;

//@Repository
public class CamRepository implements AutoCloseable, ICamRepository {

	private Logger logger = Logger.getLogger(CamRepository.class.getName());

	private List<Webcam> webcam;

	private List<BufferedImage> images = new ArrayList<>();

	public CamRepository() {
		init();
		Webcam.setAutoOpenMode(true);
	}

	public Image getImage(int index) {
		while (images.size() <= index)
			images.add(new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB));
		try {
			if (!webcam.get(index).isOpen())
				webcam.get(index).open();
			if (webcam.get(index).isImageNew())
				images.set(index, webcam.get(index).getImage());
			if (images.get(index) == null)
				throw new WebcamException("Null image");
		} catch (IndexOutOfBoundsException | WebcamException e) {
			logger.severe(e.getMessage());
			Image im = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
			im.getGraphics().drawString(e.getMessage(), 0, 240);
			return im;
		}
		return images.get(index);
	}

	private void init() {
		try {
			webcam = Webcam.getWebcams();
			webcam.parallelStream().forEach(wc -> {
				Arrays.asList(wc.getViewSizes()).forEach(wc::setViewSize);
				wc.open();
			});
		} catch (WebcamException e) {
			logger.severe(e.getMessage());
		}
	}

	@Override
	public void close() throws Exception {
		webcam.forEach(Webcam::close);
	}

	public int discover() {
		try {
			webcam = Webcam.getWebcams();
		} catch (WebcamException e) {
			logger.severe(e.getMessage());
		}
		return webcam.size();
	}

	public String getInfo(int index) {
		return webcam.get(index).getName() + " " + webcam.get(index).getViewSize() + "@" + webcam.get(index).getFPS();
	}

	@Override
	public String getImageAsString(int index) {
		return ImageUtils.getInstance().getImageAsString((BufferedImage) images.get(index));
	}

}
