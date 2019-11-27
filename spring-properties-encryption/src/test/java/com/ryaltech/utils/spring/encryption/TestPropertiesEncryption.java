package com.ryaltech.utils.spring.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestPropertiesEncryption extends BaseTest{
	@BeforeClass
	public static void init() throws Exception {
		deleteAllWorkFiles();
		copyFromClassPathToFs(PROPERTIES_FILE_NAME_SOURCE, PROPERTIES_FILE_NAME);
	}
	@Test
	public void testAppWithProperties() throws FileNotFoundException, IOException {
		Encryptor encryptor = new Encryptor();
		assertTrue(KEY_FILE+" did not get created", new File(KEY_FILE).exists());
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
