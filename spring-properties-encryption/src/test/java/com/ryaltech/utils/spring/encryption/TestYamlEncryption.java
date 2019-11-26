package com.ryaltech.utils.spring.encryption;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.ryaltech.github.security.ConfigFileEncryptor;
import com.ryaltech.github.security.Encryptor;


public class TestYamlEncryption {
	@BeforeClass
	public static void init() throws Exception {
		new File("sys.dat").delete();
		new File("application1.yml").delete();
		;
		Files.copy(Paths.get(ClassLoader.getSystemResource("application.yml.source").toURI()), Paths.get("application1.yml"));
	}

	@Test
	public void testAppWithProperties() throws FileNotFoundException, IOException {
		Encryptor encryptor = new Encryptor();		
		new ConfigFileEncryptor(encryptor).encryptConfigFile("application1.yml");
		
		/*
		try (FileInputStream fis = new FileInputStream("application1.yaml")) {
			props.load(fis);
		}
		*/
		//assertTrue(props.getProperty("password").startsWith("ENC("));

	}


}
