package com.zen.cam.detector.domain;

import java.awt.image.BufferedImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zen.capture.commons.domain.ICaptureRepository;
import com.zen.capture.commons.domain.service.ACaptureService;
import com.zen.capture.commons.utils.ImageUtils;

@Repository
public class CaptureService extends ACaptureService<BufferedImage>{

	@Autowired
	private ICaptureRepository<BufferedImage> captureRepository;

	@Override
	protected ICaptureRepository<BufferedImage> getRepository() {
		return captureRepository;
	}

	@Override
	protected String getAsString(BufferedImage image) {
		return ImageUtils.getInstance().getImageAsString(image);
	}


}
