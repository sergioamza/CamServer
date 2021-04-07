package com.zen.cam.commons.domain;

import java.awt.Image;

public interface ICamRepository {

	public Image getImage(int index);
	public String getImageAsString(int index);
	public int discover();
	public String getInfo(int index);
}
