package com.ryaltech.utils.spring.encryption;

import static org.junit.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringApplication implements CommandLineRunner{
	@Value("${property1}")
	String property1;
	@Value("${password}")
	String password;
	@Value("${test1.password}")
	String test1Password;
	@Value("${test2.password}")
	String test2Password;
	
	@Autowired
	MyComp comp;
	
	
	

	@Override
	public void run(String... args) throws Exception {
		assertEquals("value1", property1);
		assertEquals("password", password);
		assertEquals("password2", test1Password);
		assertEquals("password2", test2Password);
		assertEquals("value2", comp.property1);
		assertEquals("password2", comp.password);

	}

}
