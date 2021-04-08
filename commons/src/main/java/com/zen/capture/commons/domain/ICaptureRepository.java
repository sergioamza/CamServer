package com.zen.capture.commons.domain;

import java.awt.Image;

public interface ICaptureRepository {

	public Image getImage(int index);
	public String getImageAsString(int index);
	public int discover();
	public String getInfo(int index);
}
