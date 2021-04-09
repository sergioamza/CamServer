package com.zen.capture.server.infraestructure.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zen.capture.server.domain.ICaptureService;

@RestController
@RequestMapping("cam")
public class CaptureController {

	@Autowired
	private ICaptureService camService;

	@GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getCamImage(@PathVariable("id") int id) {
		return camService.getImageAsBase64(id);
	}

	@GetMapping(path = "{id}/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getCamName(@PathVariable("id") int id) {
		return camService.getInfo(id);
	}

	@GetMapping(path = "list")
	public List<Integer> getCamList()	{
		return camService.getCaptureList();		
	}
	
	@GetMapping("count")
	public int getCamCount() {
		return camService.discover();
	}
}
