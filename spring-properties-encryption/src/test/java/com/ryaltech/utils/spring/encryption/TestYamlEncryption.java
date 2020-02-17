package com.ryaltech.utils.spring.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestYamlEncryption extends BaseTest {
	@BeforeClass
	public static void init() throws Exception {
		deleteAllWorkFiles();
		copyFromClassPathToFs(YML_FILE_NAME_SOURCE, YML_FILE_NAME);
	}

	@Test
	public void testAppWithProperties() throws Exception{

		EncryptionConfig ec = new EncryptionConfig();
		new YamlFileEncryptor(new Encryptor(ec.getKeyFile()), ec.getIncludePatterns(), ec.getExcludePatterns()).encryptConfigFile(YML_FILE_NAME);
		assertTrue(KEY_FILE+" did not get created", new File(KEY_FILE).exists());
		
		String actualContents = new String ( Files.readAllBytes( Paths.get(YML_FILE_NAME) ) );
		String expectedContents = new String ( Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(YML_FILE_NAME_VERIFICATION).toURI() )) );
		assertEquals(expectedContents.replaceAll("ENC\\(\\w*\\)", "ENC()"), actualContents.replaceAll("ENC\\(\\w*\\)", "ENC()"));
		
	}

	@Test
	public void rerunTest() throws Exception{
		testAppWithProperties();
	}

}
