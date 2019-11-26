package com.ryaltech.utils.spring.encryption;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.ryaltech.github.security.ConfigFileEncryptor;
import com.ryaltech.github.security.Encryptor;

public class TestPropertiesEncryption {
	@BeforeClass
	public static void init() throws Exception {
		new File("sys.dat").delete();
		new File("application1.properties").delete();
		
		Files.copy(Paths.get(ClassLoader.getSystemResource("application.properties.source").toURI()), Paths.get("application1.properties"));
	}

	@Test
	public void testAppWithProperties() throws FileNotFoundException, IOException {
		Encryptor encryptor = new Encryptor();
		new ConfigFileEncryptor(encryptor).encryptConfigFile("application1.properties");
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream("application1.properties")) {
			props.load(fis);
		}
		assertTrue(props.getProperty("password").startsWith("ENC("));

	}

	@Test
	public void rerunTest() throws FileNotFoundException, IOException {
		testAppWithProperties();
	}

}
