package com.zen.cam.infraestructure;

import java.awt.Image;

import org.springframework.stereotype.Repository;

import com.zen.cam.commons.domain.ICamRepository;

@Repository
public class RemoteCamRepository implements ICamRepository {

	@Override
	public Image getImage(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int discover() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getInfo(int index) {
		// TODO Auto-generated method stub
		return null;
	}

}
