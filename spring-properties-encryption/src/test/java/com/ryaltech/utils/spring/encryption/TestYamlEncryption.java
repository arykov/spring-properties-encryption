package com.ryaltech.utils.spring.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestYamlEncryption {
	public static String YML_FILE_NAME = "application.yml";

	public static String YML_FILE_NAME_SOURCE = YML_FILE_NAME+".source";
	public static String YML_FILE_NAME_VERIFICATION = YML_FILE_NAME+".verification";
	@BeforeClass
	public static void init() throws Exception {
		new File("syskey.dat").delete();
		new File(YML_FILE_NAME).delete();
		
		Files.copy(Paths.get(ClassLoader.getSystemResource(YML_FILE_NAME_SOURCE).toURI()), Paths.get(YML_FILE_NAME));
	}

	@Test
	public void testAppWithProperties() throws Exception{
		
		Encryptor encryptor = new Encryptor();
		new YamlFileEncryptor(encryptor).encryptConfigFile(YML_FILE_NAME);
		assertTrue("syskey.dat file did not get created", new File("syskey.dat").exists());
		
		String actualContents = new String ( Files.readAllBytes( Paths.get(YML_FILE_NAME) ) );
		String expectedContents = new String ( Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(YML_FILE_NAME_VERIFICATION).toURI() )) );
		assertEquals(expectedContents.replaceAll("ENC\\(\\w*\\)", "ENC()"), actualContents.replaceAll("ENC\\(\\w*\\)", "ENC()"));
		
	}

	@Test
	public void rerunTest() throws Exception{
		testAppWithProperties();
	}

}
