package com.zen.cam.detector.infraestructure;

import java.awt.Image;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.zen.capture.commons.domain.ICaptureRepository;


@Repository
public class RemoteCamRepository implements ICaptureRepository {
	
	private final Logger logger = Logger.getLogger(RemoteCamRepository.class.getSimpleName());
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public Image getImage(int index) {
		String res = restTemplate.getForObject("http://localhost:8080/cam/" + index, String.class);
		logger.finest(res);
		return null;
	}

	public String getImageAsString(int index) {
		String res = restTemplate.getForObject("http://localhost:8080/cam/" + index, String.class);
		logger.finest(res);
		return res;
	}
	
	@Override
	public int discover() {
		int res = restTemplate.getForObject("http://localhost:8080/cam/count/", Integer.class);
		logger.finest("count: " + res);
		return res;
	}

	@Override
	public String getInfo(int index) {
		String res = restTemplate.getForObject("http://localhost:8080/cam/" + index + "/info", String.class);
		logger.finest(res);
		return res;
	}

}
