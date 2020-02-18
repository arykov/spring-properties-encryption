package com.ryaltech.utils.spring.encryption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:comp.properties")
public class MyComp {
	@Value("${property2}")
	public String property1;
	@Value("${password2}")
	public String password;

}
