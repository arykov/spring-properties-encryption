package com.ryaltech.utils.spring.encryption;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.regex.Pattern;

import org.fedorahosted.openprops.Properties;

public class PropertiesFileEncryptor extends FileEncryptor {

	public PropertiesFileEncryptor(Encryptor encryptor, Pattern[] includePatterns, Pattern[] excludePatterns) {
		super(encryptor, includePatterns, excludePatterns);
	}
	
	
	public void encryptConfigFile(String fileName) {
		Properties props = new Properties();
		boolean modified = false;
		try {
			try (FileInputStream fis = new FileInputStream(fileName)) {
				props.load(fis);
			}

			for (String key : props.keySet()) {
				if (shouldEncrypt(key)) {
					String value = props.getProperty(key);
					if (!isEncrypted(value)) {
						modified = true;
						props.setProperty(key, encrypt(value));
					}
				}
			}
			if (modified) {
				try(StringWriter baos = new StringWriter()){
					props.store(baos, null);
					save(baos.toString(), fileName);
				}
			}
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
