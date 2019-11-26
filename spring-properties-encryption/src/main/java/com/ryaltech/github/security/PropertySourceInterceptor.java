package com.ryaltech.github.security;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class PropertySourceInterceptor {
	private static Log logger = LogFactory.getLog(Encryptor.class);
	private List<String> processedNames = new ArrayList<>();
	private Encryptor encryptor = new Encryptor();
	private ConfigFileEncryptor configFileEncryptor = new ConfigFileEncryptor(encryptor);
	private Pattern sourceNamePattern = Pattern.compile(".*\\[file:(.*)\\]");
	private final boolean disableFileUpdate;

	@Before("execution(org.springframework.core.env.PropertySource.new(..)) && args(name, source)")
	public void constructorAdvice(String name, Object source) {
		if (!disableFileUpdate) {
			if (!processedNames.contains(name)) {
				processedNames.add(name);
				Matcher match = sourceNamePattern.matcher(name);
				if (match.find()) {
					String fileName = match.group(1);
					configFileEncryptor.encryptConfigFile(fileName);
				}
			}
		}

	}

	@Around("execution(public * getProperty(String)) && target(org.springframework.core.env.PropertySource+)")
	public Object getPropertyAdvice(ProceedingJoinPoint pjp) throws Throwable {
		Object retVal = pjp.proceed();
		if (retVal instanceof String) {
			return encryptor.decrypt((String) retVal);
		}
		return retVal;

	}
	
	public PropertySourceInterceptor() {
		disableFileUpdate = "false".equalsIgnoreCase(System.getProperty("enc.disableFileUpdate"));
		
	}
	

}
