package com.ryaltech.utils.spring.encryption;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.LogManager;

public class BaseTest {
	
	public static final String PROPERTIES_FILE_NAME = "application.properties";
	public static final String PROPERTIES_FILE_NAME_SOURCE = "application.properties.source";
	public static final String YML_FILE_NAME = "application.yml";
	public static final String YML_FILE_NAME_SOURCE = "application.yml.source";
	public static final String YML_FILE_NAME_VERIFICATION = "application.yml.verification";
	public static final String KEY_FILE = "syskey.dat";

	static {
		configureLogging();
	}

	public static void configureLogging() {
		try (InputStream is = BaseTest.class.getClassLoader().getResourceAsStream("logging.properties")) {
			LogManager.getLogManager().readConfiguration(is);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}

	}
	
	public static void deleteKeyFile() {
		new File(KEY_FILE).delete();
	}
	
	public static void deleteAllWorkFiles() {
		deleteKeyFile();
		new File(PROPERTIES_FILE_NAME).delete();
		new File(YML_FILE_NAME).delete();
	}
	
	public static byte[] readFullyFromClassPath(String fromFile) {
		FileSystem sourceFs = null;
		try {
			URI uri = ClassLoader.getSystemResource(fromFile).toURI();			
			Path sourcePath;
			if("jar".equals(uri.getScheme())) {
				String[] array = uri.toString().split("!");
				sourceFs = FileSystems.newFileSystem(URI.create(array[0]), new HashMap());
				sourcePath = sourceFs.getPath(array[1]);
			}else {
				sourcePath = Paths.get(uri);
			}
			
			return Files.readAllBytes(sourcePath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			if(sourceFs != null) {
				try {
					sourceFs.close();
				}catch(Exception ex) {
					
				}
			}
		}

	}
	public static void copyFromClassPathToFs(String fromFile, String toFile) {
		FileSystem sourceFs = null;
		try {
			URI uri = ClassLoader.getSystemResource(fromFile).toURI();			
			Path sourcePath;
			if("jar".equals(uri.getScheme())) {
				String[] array = uri.toString().split("!");
				sourceFs = FileSystems.newFileSystem(URI.create(array[0]), new HashMap());
				sourcePath = sourceFs.getPath(array[1]);
			}else {
				sourcePath = Paths.get(uri);
			}
			
			Files.copy(sourcePath, Paths.get(toFile));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			if(sourceFs != null) {
				try {
					sourceFs.close();
				}catch(Exception ex) {
					
				}
			}
		}
	}

}
