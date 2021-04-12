package com.zen.capture.server.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.capture.commons.domain.models.ICaptureRepository;
import com.zen.capture.commons.domain.service.ACaptureService;

@Service
public class StringCaptureService extends ACaptureService<String,String> {

	@Autowired
	private ICaptureRepository<String,String> captureRepository;

	@Override
	protected ICaptureRepository<String,String> getRepository() {
		return captureRepository;
	}

	@Override
	protected String getAsString(String image) {
		return image;
	}

}
