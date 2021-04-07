package com.zen.cam.infraestructure;

import java.awt.Image;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.zen.cam.commons.domain.ICamRepository;


@Repository
public class RemoteCamRepository implements ICamRepository {
	
	private final Logger logger = Logger.getLogger(RemoteCamRepository.class.getSimpleName());
	
	//@Autowired
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getInfo(int index) {
		// TODO Auto-generated method stub
		return null;
	}

}
