package com.zen.capture.server.domain.service;

import java.awt.image.BufferedImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.capture.commons.domain.models.ICaptureRepository;
import com.zen.capture.commons.domain.service.ACaptureService;
import com.zen.capture.commons.utils.ImageUtils;

@Service
public class BufferedImageCaptureService extends ACaptureService<BufferedImage,String> {

	@Autowired
	private ICaptureRepository<BufferedImage,String> captureRepository;

	@Override
	protected ICaptureRepository<BufferedImage,String> getRepository() {
		return captureRepository;
	}

	@Override
	protected String getAsString(BufferedImage image) {
		return ImageUtils.getInstance().getImageAsString(image);
	}

}
