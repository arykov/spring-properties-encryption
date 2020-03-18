package com.ryaltech.utils.spring.encryption;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AllFilesEncryptor extends FileEncryptor {
	private static Log logger = LogFactory.getLog(AllFilesEncryptor.class);

	private PropertiesFileEncryptor pfe;
	private YamlFileEncryptor yfe;

	String toString(Object [] objs) {
		StringBuilder sb = new StringBuilder();
		for(Object obj:objs) {
			if(sb.length()>0)sb.append(", ");
			sb.append(obj);
		}
		return sb.toString();
	}
	public AllFilesEncryptor(Encryptor encryptor, Pattern[] includePatterns, Pattern[] excludePatterns) {
		super(encryptor, includePatterns, excludePatterns);
		logger.info(String.format("Encryption patterns. Included: %s. Excluded: %s", toString(includePatterns), toString(excludePatterns)));
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
