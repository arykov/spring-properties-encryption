package com.ryaltech.utils.spring.encryption;

import java.util.regex.Pattern;

public class AllFilesEncryptor extends FileEncryptor {

	private PropertiesFileEncryptor pfe;
	private YamlFileEncryptor yfe;

	public AllFilesEncryptor(Encryptor encryptor, Pattern... propertyNamePattern) {
		super(encryptor, propertyNamePattern);
		pfe = new PropertiesFileEncryptor(encryptor, propertyNamePattern);
		yfe = new YamlFileEncryptor(encryptor, propertyNamePattern);
	}

	@Override
	public void encryptConfigFile(String fileName) {
		if (fileName.endsWith(".properties"))
			pfe.encryptConfigFile(fileName);
		else if (fileName.endsWith(".yml"))
			yfe.encryptConfigFile(fileName);
		else
			throw new RuntimeException(String.format("%s cannot be encrypted", fileName));

	}

}
