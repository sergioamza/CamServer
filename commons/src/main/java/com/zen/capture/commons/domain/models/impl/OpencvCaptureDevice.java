package com.zen.capture.commons.domain.models.impl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import com.zen.capture.commons.domain.models.ICaptureDevice;
import com.zen.capture.commons.utils.StringUtils;

/**
 * 
 * @author Sergio Martinez
 *
 * @param <T> Output format
 */
public abstract class OpencvCaptureDevice<T> implements ICaptureDevice<VideoCapture, T> {

	private static Logger logger = Logger.getLogger(OpencvCaptureDevice.class.getName());

	protected static Map<String, Integer> props = new HashMap<String, Integer>();
	{
		setProps();
	}

	protected VideoCapture captureDevice;

	public OpencvCaptureDevice() {
		super();
	}

	/**
	 * 
	 * @param id id of the video capturing device to open. To open default camera
	 *           using default backend just pass 0.(to backward compatibility usage
	 *           of camera_id + domain_offset (CAP_*) is valid when apiPreference is
	 *           CAP_ANY)implementation if multiple are available: e.g.
	 *           cv::CAP_DSHOW or cv::CAP_MSMF or cv::CAP_V4L.SEE:
	 *           cv::VideoCaptureAPIs
	 */
	public OpencvCaptureDevice(int id) {
		captureDevice = new VideoCapture(id);
		getCaptureDevice().set(Videoio.CAP_PROP_FRAME_WIDTH, 1280.0);
		getCaptureDevice().set(Videoio.CAP_PROP_FRAME_HEIGHT, 960.0);
		getCaptureDevice().set(Videoio.CAP_PROP_AUTO_EXPOSURE, 1.0);
	}

	/**
	 * 
	 * @param filename it can be: *name of video file (eg. video.avi) *or image
	 *                 sequence (eg. img_%02d.jpg, which will read samples like
	 *                 img_00.jpg, img_01.jpg, img_02.jpg, ...) - or URL of video
	 *                 stream
	 *                 (eg.protocol://host:port/script_name?script_params|auth) *or
	 *                 * GStreamer pipeline string in gst-launch tool format in case
	 *                 if GStreamer is used as backendNote that each video stream or
	 *                 IP camera feed has its own URL scheme. Please refer to
	 *                 thedocumentation of source stream to know the right
	 *                 URL.implementation if multiple are available: e.g.
	 *                 cv::CAP_FFMPEG or cv::CAP_IMAGES or cv::CAP_DSHOW.
	 * 
	 */
	public OpencvCaptureDevice(String filename) {
		captureDevice = new VideoCapture(filename);
	}

	public void close() throws Exception {
		captureDevice.release();
	}

	@Override
	public boolean isOpen() {
		return captureDevice.isOpened();
	}
	
	@Override
	public boolean open(int id)	{
		return captureDevice.open(0);
	}
	
	@Override
	public boolean open(String filename)	{
		return captureDevice.open(filename);
	}

	@Override
	public boolean isReady() {
		return ((captureDevice.isOpened()
		/* || captureDevice.open(id) */) /*
											 * && captures.get(index).getvCapture().read(image) && image.width() > 0 &&
											 * image.height() > 0
											 */);
	}

	@Override
	public void setCaptureDevice(VideoCapture device) {
		captureDevice = device;
	}

	private Mat getMatCapture() {
		Mat image = new Mat();
		captureDevice.read(image);
		return image;
	}

	@Override
	public T getCapture() {
		return convertMatToT(getMatCapture());
	}

	protected abstract T convertMatToT(Mat image);

	@Override
	public Map<String, Object> getProps() {
		Map<String, Object> cProps = new HashMap<>();
		props.forEach((k, ve) -> {
			double va = getCaptureDevice().get(ve);
			if (va != -1.0) {
				cProps.put(k, va);
			}
		});
		return cProps;
	}

	@Override
	public boolean setProp(String prop, Object val) {
		return getCaptureDevice().set((int) props.get(prop), Double.valueOf((String) val));
	}

	protected void setProps() {
		try {
			Videoio v = Videoio.class.newInstance();
			List<Field> fields = Arrays.asList(v.getClass().getFields());
			fields.stream().filter(x -> {
				return x.getName().toLowerCase().trim().startsWith("CAP".toLowerCase());
			}).forEach((x) -> {
				try {
					props.put(StringUtils.getInstance().snakeCaseToUpperCamelCase(x.getName()), x.getInt(x));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					logger.info(e.getMessage());
					e.printStackTrace();
				}
			});
		} catch (Exception e) {

		}
	}

	@Override
	public VideoCapture getCaptureDevice() {
		return captureDevice;
	}

}
