package com.samsonmarikwa.appws.security;

public class SecurityConstants {

	public static final String SIGN_UP_URL = "/users";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final long EXPIRATION_TIME = 864000000; // 10 days
	public static final String HEADER_STRING = "Authorization";

	
//	public static final SecretKey TOKEN_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
}
