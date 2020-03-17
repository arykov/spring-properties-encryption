package com.ryaltech.utils.spring.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.rules.TemporaryFolder;

public class TestCli extends BaseTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();
	
	@Test
	public void testNoParams() throws IOException {
		exit.expectSystemExitWithStatus(-1);
		Main.main();
	}

	@Test
	public void testParamsNotExistent() throws IOException {
		exit.expectSystemExitWithStatus(-1);
		Main.main("doesnotexist");
	}
	
	@Test
	public void testParamsIsFile() throws IOException {
		exit.expectSystemExitWithStatus(-1);
		File f = folder.newFile("test");
		Main.main(f.getAbsolutePath());
	}

	@Test
	public void testSeveralFiles() throws IOException, URISyntaxException {		
		File subFolder = folder.newFolder();
		File destinationPropFile = new File(subFolder, PROPERTIES_FILE_NAME);
		File destinationYmlFile = new File(subFolder, YML_FILE_NAME);
		copyFromClassPathToFs(PROPERTIES_FILE_NAME_SOURCE, destinationPropFile.getAbsolutePath());
		copyFromClassPathToFs(YML_FILE_NAME_SOURCE, destinationYmlFile.getAbsolutePath());
		Main.main(folder.getRoot().getAbsolutePath());
		
		Encryptor encryptor = new Encryptor(new File("syskey.dat"));
		assertTrue(KEY_FILE+" did not get created", new File(KEY_FILE).exists());
		
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream(destinationPropFile)) {
			props.load(fis);
		}
		assertTrue(props.getProperty("password").startsWith("ENC("));
		assertEquals("password", encryptor.decrypt(props.getProperty("password")));
		
		String actualContents = new String ( Files.readAllBytes( destinationYmlFile.toPath() ) );
		String expectedContents = new String ( Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(YML_FILE_NAME_VERIFICATION).toURI() )) );
		assertEquals(expectedContents.replaceAll("ENC\\(\\w*\\)", "ENC()").replaceAll("\r", ""), actualContents.replaceAll("ENC\\(\\w*\\)", "ENC()").replaceAll("\r", ""));

	}


}
