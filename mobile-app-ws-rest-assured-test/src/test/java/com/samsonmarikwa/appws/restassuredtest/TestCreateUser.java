package com.samsonmarikwa.appws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

class TestCreateUser {

	private final String CONTEXT_PATH = "/mobile-app-ws";

	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;

	}

	@Test
	void testCreateUser() {

		List<Map<String, Object>> userAddresses = new ArrayList<>();

		Map<String, Object> shippingAddress = new HashMap<>();
		shippingAddress.put("city", "Charlotte");
		shippingAddress.put("country", "USA");
		shippingAddress.put("streetName", "123 Matthews Common Dr");
		shippingAddress.put("postalCode", "28888");
		shippingAddress.put("type", "shipping");
		
		Map<String, Object> billingAddress = new HashMap<>();
		billingAddress.put("city", "Charlotte");
		billingAddress.put("country", "USA");
		billingAddress.put("streetName", "123 Matthews Common Dr");
		billingAddress.put("postalCode", "28888");
		billingAddress.put("type", "shipping");

		userAddresses.add(shippingAddress);
		userAddresses.add(billingAddress);

		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put("firstName", "Samson");
		userDetails.put("lastName", "Marikwa");
		userDetails.put("email", "samsonmarikwa@outlook.com");
		userDetails.put("password", "mypassword");
		userDetails.put("addresses", userAddresses);

		Response response = given().contentType("application/json")
				.accept("application/json")
				.body(userDetails)
				.when().post(CONTEXT_PATH + "/users")
				.then().statusCode(200).contentType("application/json")
				.extract()
				.response();
		
		String userId = response.jsonPath().getString("userId");
		assertNotNull(userId);
		assertTrue(userId.length() == 30);
		
		String bodyString = response.body().asString();
		try {
			JSONObject responseBodyJson = new JSONObject(bodyString);
			JSONArray addresses = responseBodyJson.getJSONArray("addresses");
			assertNotNull(addresses);
			assertTrue(addresses.length() == userAddresses.size());
			
			String addressId = addresses.getJSONObject(0).getString("addressId");
			assertNotNull(addressId);
			assertTrue(addressId.length() == 30);
			
		} catch (JSONException e) {
			fail(e.getMessage());
		}
	}

}
