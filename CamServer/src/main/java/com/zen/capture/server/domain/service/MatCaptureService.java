package com.zen.capture.server.domain.service;

import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.capture.commons.domain.models.ICaptureRepository;
import com.zen.capture.commons.domain.service.ACaptureService;
import com.zen.capture.commons.utils.ImageUtils;

@Service
public class MatCaptureService extends ACaptureService<Mat,String> {

	@Autowired
	private ICaptureRepository<Mat,String> captureRepository;

	@Override
	protected ICaptureRepository<Mat,String> getRepository() {
		return captureRepository;
	}

	@Override
	protected String getAsString(Mat image) {
		return ImageUtils.getInstance().getImageAsString(ImageUtils.getInstance().mat2BufferedImage(image));
	}

}
