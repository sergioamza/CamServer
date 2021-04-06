package com.zen.cam.detector.domain;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.cam.commons.domain.ICamRepository;




@Service
public class OpenCVService {

	@Autowired
	ICamRepository camRepository;
	
	private Logger logger = Logger.getLogger(OpenCVService.class.getName());

	public OpenCVService() {
		nu.pattern.OpenCV.loadShared();
	}

	public Image getImage(int index) {
		return camRepository.getImage(index);
	}

	public String getImageAsBase64(int index) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			byte[] pixels = ((DataBufferByte) ((BufferedImage) camRepository.getImage(index)).getRaster().getDataBuffer())
					.getData();
			logger.info("Pixels: " + pixels.length);
			Mat orig = new Mat();
			orig.put(0, 0, pixels);
			Mat gray = new Mat();
			Imgproc.cvtColor(orig, gray, Imgproc.COLOR_BGR2RGB);
			pixels = new byte[(int) gray.total()];
			gray.get(0, 0, pixels);
			BufferedImage bi = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
			ImageIO.write(bi, "jpg", bos);
			return Base64.getMimeEncoder().encodeToString(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int discover() {
		return camRepository.discover();
	}

	public String getInfo(int id) {
		return camRepository.getInfo(id);
	}

}
