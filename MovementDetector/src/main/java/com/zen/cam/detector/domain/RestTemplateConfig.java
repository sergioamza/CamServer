package com.zen.cam.detector.domain;

import java.time.Duration;
import java.util.logging.Logger;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class RestTemplateConfig {

	private final Logger logger = Logger.getLogger(RestTemplateConfig.class.getSimpleName());
	
	//@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		logger.fine("Bean init");
	    return builder
	            .setConnectTimeout(Duration.ofMillis(3000))
	            .setReadTimeout(Duration.ofMillis(3000))
	            .build();
	}
	
}
