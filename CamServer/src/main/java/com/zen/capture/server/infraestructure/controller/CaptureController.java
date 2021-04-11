package com.zen.capture.server.infraestructure.controller;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zen.capture.commons.domain.ICapture;
import com.zen.capture.commons.domain.ICaptureService;

@RestController
@RequestMapping("cam")
public class CaptureController {

	@Autowired
	private ICaptureService<BufferedImage> captureService;

	@GetMapping(path = "{id}")
	public ICapture<String> getCamImage(@PathVariable("id") int id) {
		return captureService.getCaptureString(id);
	}

	@GetMapping(path = "{id}/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getCamName(@PathVariable("id") int id) {
		return captureService.getInfo(id);
	}

	@GetMapping(path = "{id}/{prop}/{val}", produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean getCamName(@PathVariable("id") int id, @PathVariable("prop") String prop, @PathVariable("val") String val) {
		return captureService.setProp(id, prop, val);
	}

	@GetMapping(path = "list")
	public List<Integer> getCamList() {
		return captureService.getCaptureList();
	}

	@GetMapping("count")
	public int getCamCount() {
		return captureService.discover();
	}
	
	@GetMapping(path = "info", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Map<String, Object>> getCamName() {
		return captureService.getInfo();
	}
}
