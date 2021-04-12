package com.zen.cam.detector.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zen.capture.commons.domain.models.ICaptureRepository;
import com.zen.capture.commons.domain.service.ACaptureService;

@Repository
public class CaptureService extends ACaptureService<String, String> {

	@Autowired
	private ICaptureRepository<String, String> captureRepository;

	@Override
	protected ICaptureRepository<String, String> getRepository() {
		return captureRepository;
	}

	@Override
	protected String getAsString(String image) {
		return image;
	}

}
