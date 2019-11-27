package com.ryaltech.utils.spring.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

public class TestConcurrency {
	@Before
	public void setUp() {
		new File("syskey.dat").delete();
	}

	@Test
	public void shouldRunAJvm() throws Exception {
		Process process = null;
		try (RandomAccessFile raf = new RandomAccessFile("syskey.dat", "rw"); FileChannel fc = raf.getChannel()) {
			try (FileLock fl = fc.tryLock()) {
				assertNotNull(fl);
				String classpath = Arrays
						.stream(((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs())
						.map(URL::getFile).collect(Collectors.joining(File.pathSeparator));

				process = new ProcessBuilder(System.getProperty("java.home") + "/bin/java", "-classpath",
						classpath, this.getClass().getName()).inheritIO().start();
				System.out.println("Starting long process ...");
				for(int i=0;i<50;i++) {
					Thread.sleep(500);
					System.out.println("Cycle: "+i);
					assertTrue(process.isAlive());
				}
				System.out.println("Long process over ...");
			}
		}		
		assertEquals(0, process.waitFor());
		assertTrue(new File("syskey.dat").exists());		
		
	}

	public static void main(String[] args) {
		Encryptor encryptor = new Encryptor();
		System.out.println(encryptor.encrypt("no matter"));
	}

}
