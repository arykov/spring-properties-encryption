package com.ryaltech.utils.spring.encryption;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

import com.ryaltech.org.springframework.security.crypto.encrypt.Encryptors;

/**
 * Used to encrypt clear text credentials and
 * configuration to be used in spring application
 *
 * @author rykov
 *
 */
public class Encryptor {
	private Pattern encodedPattern = Pattern.compile("ENC\\((.*)\\)");
	private File secretFile;

	public Encryptor(File file) {
		this.secretFile = file;
		// Create and throw away to make sure key is created
		// TODO: if we are not encrypting key is available. This can be skipped
		new Secret(secretFile, true);
		try {
			if (Cipher.getMaxAllowedKeyLength("AES") < Integer.MAX_VALUE) {
				throw new RuntimeException("JCE Unlimited Strength not installed. ");
			}
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}

	public boolean isEncrypted(String string) {
		return encodedPattern.matcher(string).matches();
	}

	public String encrypt(String stringToEncrypt) {
		if (stringToEncrypt == null)
			return stringToEncrypt;
		// avoid double encryption
		if (!isEncrypted(stringToEncrypt)) {
			Secret s = new Secret(secretFile, false);
			return String.format("ENC(%s)",
					Encryptors.text(new String(s.password), new String(s.salt)).encrypt(stringToEncrypt));

		} else {
			return stringToEncrypt;
		}
	}

	public String decrypt(String stringToDecrypt) {
		if (stringToDecrypt == null)
			return stringToDecrypt;
		Matcher matcher = encodedPattern.matcher(stringToDecrypt);
		if (matcher.matches()) {
			Secret s = new Secret(secretFile, false);
			return Encryptors.text(new String(s.password), new String(s.salt)).decrypt(matcher.group(1));
		} else {
			return stringToDecrypt;
		}
	}
}
