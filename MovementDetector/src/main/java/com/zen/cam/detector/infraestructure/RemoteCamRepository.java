package com.zen.cam.detector.infraestructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.zen.capture.commons.domain.models.ICapture;
import com.zen.capture.commons.domain.models.ICaptureRepository;
import com.zen.capture.commons.domain.models.impl.StringCapture;

@Repository
public class RemoteCamRepository implements ICaptureRepository<String, String> {

	private final Logger logger = Logger.getLogger(RemoteCamRepository.class.getSimpleName());

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Optional<ICapture<String>> getCapture(int index) {
		StringCapture res = restTemplate.getForObject("http://localhost:8080/cam/" + index, StringCapture.class);
		return Optional.of(res);
	}

	public String getImageAsString(int index) {
		String res = restTemplate.getForObject("http://localhost:8080/cam/" + index, String.class);
		logger.info(res);
		return res;
	}

	@Override
	public int discover() {
		int res = restTemplate.getForObject("http://localhost:8080/cam/count/", Integer.class);
		logger.finest("count: " + res);
		return res;
	}

	@Override
	public List<Integer> getCaptureList() {
		@SuppressWarnings("unchecked")
		List<Integer> res = restTemplate.getForObject("http://localhost:8080/cam/list", List.class);
		return res;
	}

	@Override
	public Map<String, Object> getInfo(int index) {
		@SuppressWarnings("unchecked")
		Map<String, Object> res = restTemplate.getForObject("http://localhost:8080/cam/" + index + "/info", Map.class);
		return res;
	}

	@Override
	public Map<String, Map<String, Object>> getInfo() {
		@SuppressWarnings("unchecked")
		Map<String, Map<String, Object>> res = restTemplate.getForObject("http://localhost:8080/cam/info", Map.class);
		return res;
	}

	@Override
	public boolean setProp(int idx, String prop, String value) {
		boolean res = restTemplate.getForObject("http://localhost:8080/cam/" + idx + "/" + prop + "/" + value,
				Boolean.class);
		return res;
	}

}
