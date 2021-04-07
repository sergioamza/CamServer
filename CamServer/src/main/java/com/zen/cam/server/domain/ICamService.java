package com.zen.cam.server.domain;

import java.awt.Image;


public interface ICamService {

	public Image getImage(int index);
	public String getImageAsBase64(int index);
	public int discover();
	public String getInfo(int id);

}
