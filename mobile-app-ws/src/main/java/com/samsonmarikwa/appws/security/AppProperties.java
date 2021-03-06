package com.samsonmarikwa.appws.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

	@Autowired
	private Environment env;
	
	public String getTokenSecret() {
		return env.getProperty("tokenSecret");
	}
	
	public String getAwsAccessKey() {
		return env.getProperty("aws.access.key");
	}
	
	public String getAwsSecretKey() {
		return env.getProperty("aws.secret.key");
	}
}
