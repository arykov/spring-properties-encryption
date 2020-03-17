package com.ryaltech.utils.spring.encryption;

import java.util.regex.Pattern;

public class AllFilesEncryptor extends FileEncryptor {

	private PropertiesFileEncryptor pfe;
	private YamlFileEncryptor yfe;

	public AllFilesEncryptor(Encryptor encryptor, Pattern[] includePatterns, Pattern[] excludePatterns) {
		super(encryptor, includePatterns, excludePatterns);
		pfe = new PropertiesFileEncryptor(encryptor, includePatterns, excludePatterns);
		yfe = new YamlFileEncryptor(encryptor, includePatterns, excludePatterns);
	}

	@Override
	public void encryptConfigFile(String fileName)throws UnsupportedFileException{
		if (fileName.endsWith(".properties"))
			pfe.encryptConfigFile(fileName);
		else if (fileName.endsWith(".yml"))
			yfe.encryptConfigFile(fileName);
		else
			throw new UnsupportedFileException(String.format("%s cannot be encrypted", fileName));

	}

}
