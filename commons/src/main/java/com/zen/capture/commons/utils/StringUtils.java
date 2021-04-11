package com.zen.capture.commons.utils;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	private static final Logger logger = Logger.getLogger(StringUtils.class.getName());

	private static StringUtils instance = null;

	private StringUtils() {
	}

	public static StringUtils getInstance() {
		if (instance == null) {
			instance = new StringUtils();
		}
		return instance;
	}

	public String camelCaseToSnakeCase(String camel) {
		StringBuffer snake = new StringBuffer("");
		String regex = "([a-z])([0-9A-Z])";
		Matcher m = Pattern.compile(regex).matcher(camel);
		while(m.find())	{
			m.appendReplacement(snake, m.group(1) + "_" + m.group(2).toLowerCase());
		}
		m.appendTail(snake);
		return snake.toString();
	}
	

	public String camelCaseToUpperSnakeCase(String camel) {
		return camelCaseToSnakeCase(camel).toUpperCase();
	}

	public String snakeCaseToCamelCase(String snake) {
		StringBuffer camel = new StringBuffer("");
		String regex = "([_])([a-zA-Z0-9])";
		Matcher m = Pattern.compile(regex).matcher(snake.toLowerCase());
		while(m.find())	{
			m.appendReplacement(camel, m.group(2).toUpperCase());
		}
		m.appendTail(camel);
		return camel.toString();
	}

	public String snakeCaseToUpperCamelCase(String snake) {
		return snakeCaseToCamelCase(snake);
	}

}
