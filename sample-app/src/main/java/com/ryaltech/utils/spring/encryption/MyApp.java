package com.ryaltech.utils.spring.encryption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyApp implements CommandLineRunner{
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
		System.out.println("value1="+ property1);
		System.out.println("password="+ password);
		System.out.println("testPassword1="+ test1Password);
		System.out.println("testPassword2="+ test2Password);
		System.out.println("comp.value2="+ comp.property1);
		System.out.println("comp.password2="+ comp.password);

	}
	public static void main(String [] args){
		SpringApplication.run(MyApp.class, args);
	}

}
