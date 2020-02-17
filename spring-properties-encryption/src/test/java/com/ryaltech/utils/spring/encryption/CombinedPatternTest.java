package com.ryaltech.utils.spring.encryption;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Illustration of pattern with exceptions
 * 
 * @author arykov
 *
 */
public class CombinedPatternTest {
	
	public static void main(String[] args) throws Exception{
		Manifest mf = new Manifest();
		
		Attributes attrs = new Attributes();
		attrs.put(new Attributes.Name("test"), "hello");
		mf.getEntries().put("test", attrs);
		mf.write(System.out);
		
	}
	

}
