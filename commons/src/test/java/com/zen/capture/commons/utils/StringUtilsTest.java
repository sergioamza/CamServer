package com.zen.capture.commons.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StringUtilsTest {
	StringUtils su = StringUtils.getInstance();

	@Test
	void testCamelCaseToSnakeCase() {
		assertEquals("simple_snake_case", su.camelCaseToSnakeCase("simpleSnakeCase"));
		assertEquals("_simple_snake_case", su.camelCaseToSnakeCase("_simpleSnakeCase"));
		assertEquals("SIMPLE_SNAKE_CASE", su.camelCaseToUpperSnakeCase("simpleSnakeCase"));	
		assertEquals("SIMPLE_SNAKE_CASE", su.camelCaseToUpperSnakeCase("SIMPLE_SNAKE_CASE"));		
	}
	
	void testSnakeCaseToCamelCase()	{
		assertEquals("simpleSnakeCase", su.snakeCaseToCamelCase("simple_snake_case"));
		assertEquals("simpleSnakeCaseA", su.snakeCaseToCamelCase("SIMPLE_SNAKE_CASE_A"));
		assertEquals("simpleSnakeCaseA", su.snakeCaseToCamelCase("simpleSnakeCaseA"));
		assertEquals("_simpleSnakeCase", su.snakeCaseToCamelCase("_simple_snake_case"));
	}

}
