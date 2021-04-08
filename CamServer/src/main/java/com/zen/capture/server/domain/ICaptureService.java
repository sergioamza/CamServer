package com.zen.capture.server.domain;

import java.awt.Image;


public interface ICaptureService {

	public Image getImage(int index);
	public String getImageAsBase64(int index);
	public int discover();
	public String getInfo(int id);

}
