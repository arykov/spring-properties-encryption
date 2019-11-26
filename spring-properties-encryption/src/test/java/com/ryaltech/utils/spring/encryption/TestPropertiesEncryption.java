package com.ryaltech.utils.spring.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ryaltech.utils.spring.encryption.Encryptor;
import com.ryaltech.utils.spring.encryption.PropertiesFileEncryptor;

public class TestPropertiesEncryption {
	private static final String PROPERTIES_FILE_NAME =  "application.properties";
	private static final String PROPERTIES_FILE_NAME_SOURCE =  "application.properties.source";
	@BeforeClass
	public static void init() throws Exception {
		new File("syskey.dat").delete();
		new File(PROPERTIES_FILE_NAME).delete();
		
		Files.copy(Paths.get(ClassLoader.getSystemResource(PROPERTIES_FILE_NAME_SOURCE).toURI()), Paths.get(PROPERTIES_FILE_NAME));
	}

	@Test
	public void testAppWithProperties() throws FileNotFoundException, IOException {
		Encryptor encryptor = new Encryptor();
		new PropertiesFileEncryptor(encryptor).encryptConfigFile(PROPERTIES_FILE_NAME);
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE_NAME)) {
			props.load(fis);
		}
		assertTrue(props.getProperty("password").startsWith("ENC("));
		assertEquals("password", encryptor.decrypt(props.getProperty("password")));
	}

	@Test
	public void rerunTest() throws FileNotFoundException, IOException {
		testAppWithProperties();
	}

}
