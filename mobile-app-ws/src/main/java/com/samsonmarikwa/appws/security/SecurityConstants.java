package com.samsonmarikwa.appws.security;

public class SecurityConstants {

	public static final String SIGN_UP_URL = "/users";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final long EXPIRATION_TIME = 864000000; // 10 days
	public static final long PASSWORD_RESET_EXPIRATION_TIME = 1000*60*60; // 1 HOUR (1000Milliseconds=1Sec * 60 * 60)
	public static final String HEADER_STRING = "Authorization";
	public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
	public static final String PASSWORD_RESET_REQUEST_URL = "/users/password-reset-request";

	
//	public static final SecretKey TOKEN_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
}
