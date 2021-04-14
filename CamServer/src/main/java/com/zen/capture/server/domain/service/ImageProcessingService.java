package com.zen.capture.server.domain.service;

import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.capture.commons.domain.models.ICapture;
import com.zen.capture.commons.domain.models.ICaptureService;
import com.zen.capture.commons.domain.models.impl.MatCapture;
import com.zen.capture.commons.domain.models.impl.StringCapture;
import com.zen.capture.commons.utils.ImageUtils;

@Service
public class ImageProcessingService {

	@Autowired
	private ICaptureService<Mat, String> matCaptureService;

	private static final ImageUtils imageUtils = ImageUtils.getInstance();

	public ICapture<String> getDifferentialMat(int index, int stepFrom, int stepTo) {
		final ICapture<Mat> capFrom = matCaptureService.getCapture(index, stepFrom);
		final ICapture<Mat> capTo = matCaptureService.getCapture(index, stepTo);
		return getStringCapture(differential(capFrom, capTo));
	}

	public ICapture<Mat> differential(ICapture<Mat> from, ICapture<Mat> sub) {
		ICapture<Mat> difference = new MatCapture();
		if (from != null && sub != null && (from.getCaptureTime() - sub.getCaptureTime()) != 0) {
			difference.setCaptureTime(from.getCaptureTime() - sub.getCaptureTime());
			difference.setImage((from.getImage() != null && sub.getImage() != null)
					? imageUtils.matDiff(
							imageUtils.mat2ThresMat(
									imageUtils.mat2GrayMat(imageUtils.matResize(from.getImage() , 160, 120 ))),
							imageUtils.mat2ThresMat(
									imageUtils.mat2GrayMat(imageUtils.matResize(sub.getImage() , 160, 120 ))))
					: null);
			/*difference.setImage((from.getImage() != null)
					? imageUtils.drawContoursAndFilter(from.getImage(), difference.getImage())
					: null);*/
			return difference;
		} else {
			return null;
		}
	}

	public ICapture<String> getStringCapture(ICapture<Mat> in) {
		if (in == null)
			return null;
		ICapture<String> difference = new StringCapture();
		difference.setImage(
				in.getImage() != null ? imageUtils.getImageAsString(imageUtils.matToBufferedImage(in.getImage()))
						: null);
		difference.setCaptureTime(in.getCaptureTime());
		return difference;
	}
}
