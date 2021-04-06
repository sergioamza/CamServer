package com.zen.cam.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zen.cam.server.service.CamService;

@RestController
@RequestMapping("cam")
public class CamController {

	@Autowired
	private CamService camService;

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getCamImage(@PathVariable("id") int id) {
		return camService.getImageAsBase64(id);
	}

	@GetMapping(path = "/{id}/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getCamName(@PathVariable("id") int id) {
		return camService.getInfo(id);
	}

	@GetMapping("/count")
	public int getCamCount() {
		return camService.discover();
	}
}
