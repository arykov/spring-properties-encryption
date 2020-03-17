package com.ryaltech.utils.spring.encryption;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YamlFileEncryptor extends FileEncryptor {
	private DumperOptions dumperOptions;
	public YamlFileEncryptor(Encryptor encryptor, Pattern[] includePatterns, Pattern[] excludePatterns) {
		super(encryptor, includePatterns, excludePatterns);	
		dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		dumperOptions.setIndent(4);
		dumperOptions.setIndicatorIndent(2);
		dumperOptions.setPrettyFlow(true);

	}

	@Override
	public void encryptConfigFile(String fileName) {
		Yaml yaml = new Yaml(dumperOptions);
		try {

			List<Object> maps = new ArrayList<>();
			try (InputStream fis = new FileInputStream(fileName)) {
				for (Object map : yaml.loadAll(fis)) {
					maps.add(map);
				}
			}
			boolean modified = false;
			for (Object map : maps) {
				modified = modified | encrypt((Map<String, Object>) map, "");
			}
			if (modified) {
				save(yaml.dumpAll(maps.iterator()), fileName);

			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	String combineKeys(String baseKey, String key) {
		if (baseKey == null)
			return key;
		else
			return baseKey + "." + key;

	}

	boolean encrypt(Map<String, Object> map, String baseKey) {
		boolean modified = false;
		for (String key : map.keySet()) {
			String fullKey = combineKeys(baseKey, key);
			Object value = map.get(key);
			if (value instanceof String ) {
				String newValue = encrypt((String)value, fullKey);
				if(newValue != null) {
					map.put(key, newValue);
					modified = true;
				}								
			}else if(value instanceof List) {
				modified = modified | encrypt((List)value, fullKey);
			}else if(value instanceof Map) {
				modified = modified | encrypt((Map)value, fullKey);
			}
		}
		return modified;
	}
	
	String encrypt(String value, String baseKey) {
		if(shouldEncrypt(baseKey)) {
			if(!isEncrypted(value)) {
				return encrypt(value);				
			}
		}
		return null;
	}
	
	
	boolean encrypt(List values, String baseKey) {
		boolean modified = false;
		for(int i=0;i<values.size();i++) {
			String fullKey = baseKey+"["+i+"]";			
			Object value=values.get(i);
			if(value instanceof String) {
				String newValue = encrypt((String)value, fullKey);
				if(newValue != null) {
					values.set(i, newValue);
					modified = true;
				}						
			}else if(value instanceof List) {
				modified = modified | encrypt((List)value, fullKey);
			}else if(value instanceof Map) {
				modified = modified | encrypt((Map)value, fullKey);
			}
		}
		return modified;	
	}
}
