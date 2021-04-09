package com.zen.capture.server.domain;

import java.awt.Image;
import java.util.List;


public interface ICaptureService {

	public Image getImage(int index);
	public String getImageAsBase64(int index);
	public int discover();
	public String getInfo(int id);
	public List<Integer> getCaptureList();

}
