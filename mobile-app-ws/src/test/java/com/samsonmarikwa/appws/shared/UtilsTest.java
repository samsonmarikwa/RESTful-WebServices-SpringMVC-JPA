package com.samsonmarikwa.appws.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)	// loads spring context, required for integration testing
@SpringBootTest
class UtilsTest {
	
	@Autowired
	Utils utils;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	 // This method under test does not need an integration test because we are not accessing
	// properties file, so we can remove the annotations at the top of the class
	void testGenerateUserId() {
		String userId = utils.generateUserId(30);
		String userId2 = utils.generateUserId(30);
		assertNotNull(userId);
		assertTrue(userId.length() == 30);
		assertTrue( !userId.equalsIgnoreCase(userId2) );
		
	}

	@Test
	// The method under test requires access to the spring context to read the token secret used to validate the token
	// , so this is an integration test.
	void testHasTokenNotExpired() {
		String token = utils.generateEmailVerificationToken("user-id");
		assertNotNull(token);
		
		boolean hasTokenExpired = Utils.hasTokenExpired(token);	
		assertFalse(hasTokenExpired);
	}
	
	@Test
	void testHasTokenExpired() {
		// The hardcoded token will expire over time and this test should be valid then.
		String expiredToken = utils.generateEmailVerificationToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoeTk1b3k5MVd5c2lBYUc0NUV5MXBCN04wZXp6anMiLCJleHAiOjE2NTYzODcwMDd9.VMe-KT2wNn2GkrYrNZPS06MWK6Cbzs5Z983eDGSKzeJJC-e7ARF-YfLMMxKKi86ISq7w2r-SPTogaO4kHlqOJg");
		assertNotNull(expiredToken);
		
		boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);	
		assertTrue(hasTokenExpired);
	}

}
