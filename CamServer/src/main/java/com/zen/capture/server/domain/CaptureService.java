package com.zen.capture.server.domain;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.capture.commons.domain.ICaptureRepository;

@Service
public class CaptureService implements ICaptureService {

	@Autowired
	private ICaptureRepository captureRepository;

	public Image getImage(int index) {
		return captureRepository.getImage(index);
	}

	public String getImageAsBase64(int index) {
		return captureRepository.getImageAsString(index);
	}

	public int discover() {
		return captureRepository.discover();
	}

	public String getInfo(int id) {
		return captureRepository.getInfo(id);
	}

}
