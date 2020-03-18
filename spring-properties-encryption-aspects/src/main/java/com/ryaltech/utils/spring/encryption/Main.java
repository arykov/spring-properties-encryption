package com.ryaltech.utils.spring.encryption;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.beust.jcommander.JCommander;

public class Main {
	private static Log logger = LogFactory.getLog(Main.class);

	static Pattern[] mergePatterns(Pattern [] defaultPatterns, List<Pattern> overrideDefaultPatterns, List<Pattern> additionalPaterns) {
		List<Pattern> patterns = new ArrayList<>();
		if(overrideDefaultPatterns != null )patterns.addAll(overrideDefaultPatterns);
		else if(defaultPatterns != null )patterns.addAll(Arrays.asList(defaultPatterns));
		if(additionalPaterns != null)patterns.addAll(additionalPaterns);		
		return patterns.toArray(new Pattern[patterns.size()]);
		
	}
	public static void main(String... argv) throws IOException {
		Args args = new Args();		
		JCommander jc = JCommander.newBuilder().addObject(args).build();
		try {
			jc.parse(argv);
		}catch(Exception ex) {
			jc.usage();
			System.exit(-1);
			return;
		}
		EncryptionConfig config = new EncryptionConfig();		
		Encryptor encryptor = new Encryptor(config.getKeyFile());
		if(!validate(args)) {
			System.exit(-1);			
		}		
		
		final AllFilesEncryptor configFileEncryptor = new AllFilesEncryptor(encryptor,
				mergePatterns(config.getIncludePatterns(), args.includePatterns, args.additionalIncludePatterns),
				mergePatterns(config.getExcludePatterns(), args.excludePatterns, args.additionalExcludePatterns));
		for (File source : args.sources) {
			
			if(source.isDirectory()) {
				Files.walk(source.toPath()).filter(path -> Files.isRegularFile(path)).forEach(path -> {
					logger.info("Processing directory: " + source);
					encryptFile(configFileEncryptor, path);
				});
			}else {
				logger.info("Processing file: " + source);
				encryptFile(configFileEncryptor, source.toPath());
				
			}
		}
	}

	private static void encryptFile(final AllFilesEncryptor configFileEncryptor, Path path) {
		String fileName = path.toString();
		logger.info("Found file: " + fileName);
		try {
			configFileEncryptor.encryptConfigFile(path.toString());
			logger.info("Succesfully encrypted " + fileName);
		} catch (UnsupportedFileException e) {
			logger.info("Encryption for the following file is not supported  " + fileName);
		}
	}

	private static boolean validate(Args args) {
		boolean retVal = true;
		for (File configRootFile : args.sources) {
			if (!configRootFile.exists()) {
				retVal = false;
				logger.error(configRootFile+" does not exist.");
			}else if(!configRootFile.canRead() || !configRootFile.canWrite()) {
				retVal = false;
				logger.error(configRootFile+" cannot be read or written to.");
			}
		}
		return retVal;
	}

}
