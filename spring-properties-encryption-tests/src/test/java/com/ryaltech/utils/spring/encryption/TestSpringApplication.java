package com.ryaltech.utils.spring.encryption;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class TestSpringApplication {
	@BeforeClass
	public static void init() throws Exception {
		new File("sys.dat").delete();
		new File("application.properties").delete();
		new File("comp.properties").delete();
		
		Files.copy(Paths.get(ClassLoader.getSystemResource("application.properties.source").toURI()), Paths.get("application.properties"));
		Files.copy(Paths.get(ClassLoader.getSystemResource("comp.properties.source").toURI()), Paths.get("comp.properties"));
	}

	@Test
	public void test1() throws Exception{
		new SpringApplicationBuilder().sources(SpringApplication.class).build().run();
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream("application.properties")) {
			props.load(fis);
		}
		assertTrue(props.getProperty("password").startsWith("ENC("));

	}

}
