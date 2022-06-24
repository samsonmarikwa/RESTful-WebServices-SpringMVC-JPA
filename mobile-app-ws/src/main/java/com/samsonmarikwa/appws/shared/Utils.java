package com.samsonmarikwa.appws.shared;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.samsonmarikwa.appws.SpringApplicationContext;
import com.samsonmarikwa.appws.security.AppProperties;
import com.samsonmarikwa.appws.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Utils {
	
	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	public String generateId(int length) {
		return generateRandomString(length);
	}

	private String generateRandomString(int length) {
		StringBuilder returnValue = new StringBuilder(length);
		
		for (int i=0; i<length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		
		return new String(returnValue);
	}

	public static boolean hasTokenExpired(String token) {
		
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
		
		Claims claims = Jwts
				.parser()
				.setSigningKey(appProperties.getTokenSecret())
				.parseClaimsJws(token)
				.getBody();
		
		Date tokenExpirationDate = claims.getExpiration();
		Date todayDate = new Date();
		return tokenExpirationDate.before(todayDate);
		
	}

	public String generateEmailVerificationToken(String userId) {
		
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
		String tokenSecret = appProperties.getTokenSecret();
		String token = Jwts.builder()
				.setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, tokenSecret)
				.compact();
		
		return token;
	}

	public String generatePasswordResetToken(String userId) {
		
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
		String tokenSecret = appProperties.getTokenSecret();
		String token = Jwts.builder()
				.setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, tokenSecret)
				.compact();
		
		return token;
	}

}
