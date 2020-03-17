package com.ryaltech.utils.spring.encryption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {
	private static Log logger = LogFactory.getLog(Main.class);

	public static void helpAndExit() {
		System.err.println("Expect a single parameter pointing at root directory.");
		System.exit(-1);
	}

	public static void main(String ... args) throws IOException {
		if (args.length != 1 || !isDir(args[0])) {
			helpAndExit();
		}
		Path configRootPath = Paths.get(args[0]);
		EncryptionConfig config = new EncryptionConfig();
		Encryptor encryptor = new Encryptor(config.getKeyFile());
		final AllFilesEncryptor configFileEncryptor = new AllFilesEncryptor(encryptor, config.getIncludePatterns(),
				config.getExcludePatterns());
		Files.walk(configRootPath).filter(path -> Files.isRegularFile(path)).forEach(path -> {
			String fileName = path.toString();
			logger.info("Found file: " + fileName);
			try {
				configFileEncryptor.encryptConfigFile(path.toString());
				logger.info("Succesfully encrypted " + fileName);
			} catch (UnsupportedFileException e) {
				logger.info("Encryption for the following file is not supported  " + fileName);
			}
		});
	}

	private static boolean isDir(String string) {
		Path path = Paths.get(string);
		return Files.exists(path) && Files.isDirectory(path);
	}
}
