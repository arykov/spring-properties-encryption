package com.ryaltech.utils.spring.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

public class TestThreadConcurrency extends BaseTest implements UncaughtExceptionHandler{
	Throwable threadThrowable;
	
	@Before
	public void setUp() {
		deleteKeyFile();
	}
	
	private Object synchLock = new Object();
	public void run()  {
		try (RandomAccessFile raf = new RandomAccessFile(KEY_FILE, "rw"); FileChannel fc = raf.getChannel()) {
			try (FileLock fl = fc.tryLock()) {				
				assertNotNull(fl);
				//notify once file lock is acquired
				synchronized(synchLock) {
					synchLock.notify();
				}
				
				System.out.println("Starting long process ...");
				for(int i=0;i<21;i++) {
					Thread.sleep(500);
					System.out.println("Cycle: "+i);

				}
				System.out.println("Long process over ...");
			}
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}

		
	}
	
	@Test
	public void testLockAndRunThread() throws Throwable {
		Thread th = new Thread(()->run());
		th.setUncaughtExceptionHandler(this);
		synchronized(synchLock) {
			th.start();
			synchLock.wait(1000);
		}
		assertNoThreadException();
		assertTrue("thread should still be alive", th.isAlive());			
		System.out.println(new Encryptor().encrypt("lalala"));
		assertNoThreadException();
		assertFalse("thread should have finished by now alive", th.isAlive());

		
		assertTrue(new File(KEY_FILE).exists());		
		
	}
	private void assertNoThreadException() throws Throwable{
		if(threadThrowable != null) throw threadThrowable;		
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable th) {
		threadThrowable = th;
		
	}


}
