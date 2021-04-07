package com.zen.cam.server.domain;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.cam.commons.domain.ICamRepository;

@Service
public class CamService implements ICamService {

	@Autowired
	private ICamRepository camRepo;

	public Image getImage(int index) {
		return camRepo.getImage(index);
	}

	public String getImageAsBase64(int index) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			ImageIO.write((BufferedImage) getImage(index), "jpg", bos);
			return Base64.getMimeEncoder().encodeToString(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int discover() {
		return camRepo.discover();
	}

	public String getInfo(int id) {
		return camRepo.getInfo(id);
	}

}
