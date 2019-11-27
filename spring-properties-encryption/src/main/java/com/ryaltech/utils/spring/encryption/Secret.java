package com.ryaltech.utils.spring.encryption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
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
	private final byte[] toBytes() {
		assert salt != null && password != null : "salt and or password are not properly initialized";

		// not doing close to avoid closing underlying stream

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(salt);
			oos.writeObject(password);
			oos.flush();
			return baos.toByteArray();
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
			read(ois);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private final void read(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		salt = (char[]) ois.readObject();
		password = (char[]) ois.readObject();
	}

	Secret() {
		this(false);
	}
	
	private static final long SLEEP_BETWEEN_LOCK_ATTEMPTS = 50;
	private static final int LOCK_ATTEMPTS = 1000;

	Secret(boolean createKeyIfNeeded) {
		keyFile = new File(System.getProperties().getProperty("enc.sysKeyFile", "syskey.dat"));
		if (createKeyIfNeeded) {
			//50 seconds is a bit excessive
			for (int i = 0; i < LOCK_ATTEMPTS; i++) {
				try (RandomAccessFile raf = new RandomAccessFile(keyFile, "rw");
						FileChannel channel = raf.getChannel();
						InputStream is = Channels.newInputStream(channel);
						OutputStream os = Channels.newOutputStream(channel)) {
					// TODO: currently if multiple instances of this are used in the VM and one
					// tries to acquire lock at the same time it will cause the second instance to
					// fail
					FileLock lock = channel.tryLock();
					if (lock != null) {
						// Both writing and reading is done through a buffer to avoid the problem when
						// closing streams causes underlying streams to be closed. The alternative of
						// keeping them open is not appealing. Better solution?
						try {
							// counting on normal size files produced by us
							byte[] fileContents = new byte[(int) keyFile.length()];
							is.read(fileContents);
							try (ByteArrayInputStream bais = new ByteArrayInputStream(fileContents);
									ObjectInputStream ois = new ObjectInputStream(bais)) {
								read(ois);
							}
						} catch (Exception ex) {
							logger.debug(ex);
							salt = Hex.encode(KeyGenerators.secureRandom(16).generateKey());
							password = Hex.encode(KeyGenerators.secureRandom(64).generateKey());
							os.write(toBytes());
							if (!FileSystems.getDefault().supportedFileAttributeViews().contains("posix")) {
								logger.error(
										"Failed to make "+keyFile.getName()+" readable only by owner(400). Please, make sure it is done manually.");
							} else {
								Files.setPosixFilePermissions(keyFile.toPath(), Collections.singleton(PosixFilePermission.OWNER_READ));
							}

						} finally {
							lock.release();
						}
						return;
					}
					if(i%(LOCK_ATTEMPTS/10) == 0 && i>0) {
						logger.warn("Lock is being held excessively long ... Seconds waiting: "+i*SLEEP_BETWEEN_LOCK_ATTEMPTS/1000);						
					}
					Thread.sleep(SLEEP_BETWEEN_LOCK_ATTEMPTS);
					
				} catch (IOException | InterruptedException ex) {
					logger.error("exception", ex);
					throw new RuntimeException(ex);
				}
			}

		} else {
			read(keyFile);
		}
	}
}
