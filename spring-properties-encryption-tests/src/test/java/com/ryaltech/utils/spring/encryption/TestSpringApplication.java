package com.ryaltech.utils.spring.encryption;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Does not run properly within Eclipse
 * @author arykov
 *
 */
public class TestSpringApplication extends BaseTest{
	@Before
	public void init() throws Exception {
		deleteAllWorkFiles();
		new File("comp.properties").delete();
		copyFromClassPathToFs(PROPERTIES_FILE_NAME_SOURCE, PROPERTIES_FILE_NAME);
		copyFromClassPathToFs("comp.properties.source", "comp.properties");
	}

	@Test
	public void test1() throws Exception{
		new SpringApplicationBuilder().sources(SpringApplication.class).build().run();
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE_NAME)) {
			props.load(fis);
			assertTrue(props.getProperty("password").startsWith("ENC("));
		}
		

	}

}
