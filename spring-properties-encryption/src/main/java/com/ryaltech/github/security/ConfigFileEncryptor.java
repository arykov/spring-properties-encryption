package com.ryaltech.github.security;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigFileEncryptor {
	private static final Pattern[] defaultPatterns = { Pattern.compile(".*password", Pattern.CASE_INSENSITIVE) };
	private Pattern[] patterns = defaultPatterns;
	private Encryptor encryptor;

	public ConfigFileEncryptor(Encryptor encryptor) {
		this.encryptor = encryptor;
	}

	public ConfigFileEncryptor(Encryptor encryptor, Pattern... propertyNamePattern) {
		this(encryptor);
		this.patterns = propertyNamePattern;
	}

	public boolean shouldEncrypt(String key) {
		for (Pattern p : patterns) {
			Matcher m = p.matcher(key);
			if (m.matches()) {
				return true;
			}
		}
		return false;

	}

	Class<? extends FileBasedConfiguration> configClassForFile(String fileName) {
		if (fileName.endsWith(".properties")) {
			return PropertiesConfiguration.class;
		} else if (fileName.endsWith(".yml")) {
			return YAMLConfiguration.class;
		} else {
			throw new RuntimeException(
					String.format("Based on extension %s is neither properties nor yaml file", fileName));
		}

	}

	public void encryptConfigFile(String fileName) {
		try {
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
					configClassForFile(fileName)).configure(
							new Parameters().properties().setFileName(fileName).setThrowExceptionOnMissing(true));
			Configuration config = builder.getConfiguration();

			boolean anyChanges = false;
			Map<String, String> modifiedValues = new HashMap<>();
			for (Iterator<String> it = config.getKeys(); it.hasNext();) {
				String keyString = it.next();
				if (shouldEncrypt(keyString)) {
					Object value = config.get(Object.class, keyString);
					if (value instanceof String) {
						String strValue = (String) value;
						if (!encryptor.isEncrypted(strValue)) {
							anyChanges = true;
							modifiedValues.put(keyString, encryptor.encrypt(strValue));
						}
					}
				}
			}
			if (anyChanges) {
				for (String key : modifiedValues.keySet()) {
					config.setProperty(key, modifiedValues.get(key));
				}
				
				builder.save();
			}
		} catch (ConfigurationException ex) {
			throw new RuntimeException(ex);
		}
	}
}
