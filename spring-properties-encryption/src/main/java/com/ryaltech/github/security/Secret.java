package com.ryaltech.github.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ryaltech.org.springframework.security.crypto.codec.Hex;
import com.ryaltech.org.springframework.security.crypto.keygen.KeyGenerators;

class Secret {
	private static Log logger = LogFactory.getLog(Secret.class);
	private final File keyFile;;
	char[] salt, password;

	/**
	 * Save to file
	 *
	 * @param f
	 * @param data
	 * @throws Exception
	 */
	private final void save(File f) {
		assert salt != null && password != null : "salt and or password are not properly initialized";

		try ( 	FileOutputStream fos = new FileOutputStream(f);				
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(salt);
			oos.writeObject(password);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Read from file
	 *
	 * @param f
	 * @return
	 * @throws Exception
	 */
	private final void read(File f) {
		try (FileInputStream fis = new FileInputStream(f); ObjectInputStream ois = new ObjectInputStream(fis)) {
			salt = (char[]) ois.readObject();
			password = (char[]) ois.readObject();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	
	Secret() {
		keyFile = new File(System.getProperties().getProperty("enc.sysKeyFile", "syskey.dat"));
		try {
			if (!keyFile.exists()) {
				salt = Hex.encode(KeyGenerators.secureRandom(16).generateKey());
				password = Hex.encode(KeyGenerators.secureRandom(64).generateKey());
				save(keyFile);
			}
			if (!FileSystems.getDefault().supportedFileAttributeViews().contains("posix")) {
				logger.error(
						"Failed to make sys.dat is readable only by owner(400). Please, make sure it is done manually.");
			} else {
				Files.setPosixFilePermissions(keyFile.toPath(), Collections.singleton(PosixFilePermission.OWNER_READ));
			}

		} catch (IOException ex) {
			throw new RuntimeException("sys.dat should have permissions to read for the owner only(400)", ex);

		}
		read(keyFile);
	}

}
