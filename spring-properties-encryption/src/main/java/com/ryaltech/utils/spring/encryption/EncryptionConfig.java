package com.ryaltech.utils.spring.encryption;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * contains all configuration
 * reads it from contained config.yml
 * @author arykov
 *
 */
public class EncryptionConfig {
	private static final Log logger = LogFactory.getLog(EncryptionConfig.class);
	private final String READ_ONLY_KEY = "readOnly", KEY_FILE_KEY = "keyFile", INCLUDE_PATTERNS_KEY = "includePatterns",
			EXCLUDE_PATTERNS_KEY = "excludePatterns";	
	private static final List<String> defaultPatterns = new ArrayList<String>() {
		{
			add(".*password[^a-z]*");
			add(".*passphrase[^a-z]*");
		}
	};
	private boolean readOnly;
	private Pattern[] includePatterns;
	private Pattern[] excludePatterns;
	private File keyFile;
	public boolean isReadOnly() {
		return readOnly;
	}

	public Pattern[] getIncludePatterns() {
		return includePatterns;
	}

	public Pattern[] getExcludePatterns() {
		return excludePatterns;
	}

	public File getKeyFile() {
		return keyFile;
	}

	public EncryptionConfig() {
		try (InputStream in = getClass()
				.getResourceAsStream("/" + getClass().getPackage().getName().replaceAll("\\.", "/") + "/config.yml")) {
			logger.info("Reading config file");
			Yaml yaml = new Yaml();
			Map<String, Object> map = yaml.load(in);
			logger.info(String.format("Reading config for %s", READ_ONLY_KEY));
			readOnly = (Boolean) map.getOrDefault(READ_ONLY_KEY, false);
			logger.info(String.format("Reading config for %s", INCLUDE_PATTERNS_KEY));
			List<String>includPatternsStr = (List<String>)map.get(INCLUDE_PATTERNS_KEY);
			includPatternsStr = includPatternsStr == null?Collections.EMPTY_LIST:includPatternsStr;
			includePatterns = toPatterns(includPatternsStr);
			logger.info(String.format("Reading config for %s", EXCLUDE_PATTERNS_KEY));
			List<String>excludPatternsStr = (List<String>)map.get(EXCLUDE_PATTERNS_KEY);
			excludPatternsStr = excludPatternsStr == null?Collections.EMPTY_LIST:excludPatternsStr;
			excludePatterns = toPatterns(excludPatternsStr);
			keyFile = new File((String)map.getOrDefault(KEY_FILE_KEY, "syskey.dat"));
			
		} catch (IOException ex) {
			logger.error(ex);
			throw new RuntimeException(ex);
		}
	}

	private Pattern[] toPatterns(List<String> patternStrs) {
		Pattern[] patterns = new Pattern[patternStrs.size()];
		for (int i = 0; i < patternStrs.size(); i++) {
			patterns[i] = Pattern.compile(patternStrs.get(i), Pattern.CASE_INSENSITIVE);
		}
		return patterns;
	}

	public static void main(String[] args) {
		new EncryptionConfig();
	}

}
