package com.zen.cam.infraestructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zen.cam.commons.domain.ICamRepository;

@RestController
@RequestMapping("cam")
public class DetectorController {

	@Autowired
	private ICamRepository camRepository;

	@GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getImage(@PathVariable("id") int index) {
		return camRepository.getImageAsString(index);
	}

	@GetMapping(path = "{id}/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getCamName(@PathVariable("id") int id) {
		return camRepository.getInfo(id);
	}

	@GetMapping("count")
	public int getCamCount() {
		return camRepository.discover();
	}
}
