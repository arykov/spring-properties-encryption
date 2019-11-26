package com.ryaltech.utils.spring.encryption;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class FileEncryptor {
	private static final Log logger = LogFactory.getLog(FileEncryptor.class);
	private static final Pattern[] defaultPatterns = { Pattern.compile(".*password[^a-z]*", Pattern.CASE_INSENSITIVE) };
	private Pattern[] patterns = defaultPatterns;
	private Encryptor encryptor;

	public FileEncryptor(Encryptor encryptor, Pattern... propertyNamePattern) {
		this.encryptor = encryptor;
		if (propertyNamePattern.length > 0)
			this.patterns = propertyNamePattern;
	}

	boolean shouldEncrypt(String key) {
		for (Pattern p : patterns) {
			Matcher m = p.matcher(key);
			if (m.matches()) {
				return true;
			}
		}
		return false;
	}

	String encrypt(String stringToEncrypt) {
		return encryptor.encrypt(stringToEncrypt);
	}

	boolean isEncrypted(String stringTorEncrypt) {
		return encryptor.isEncrypted(stringTorEncrypt);
	}

	
	void save(String payload, String fileName) throws IOException {
		File dest = new File(fileName).getAbsoluteFile();
		File dir = dest.getParentFile();
		File tempFile = File.createTempFile("encconfig", ".tmp", dir);
		try (FileWriter fos = new FileWriter(tempFile)) {
			fos.write(payload);
		}
		Files.move(tempFile.toPath(), dest.toPath(), ATOMIC_MOVE, REPLACE_EXISTING);
	}

	public abstract void encryptConfigFile(String fileName);
}
