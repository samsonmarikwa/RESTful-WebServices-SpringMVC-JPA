package com.samsonmarikwa.appws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsersWebServiceEndpointTest {

	private final String CONTEXT_PATH = "/mobile-app-ws";
	private final String EMAIL_ADDRESS = "samsonmarikwa@outlook.com";
	private final String JSON = "application/json";

	private static String authorizationHeader;
	private static String userId;
	private static List<Map<String, String>> addresses;

	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;

	}

	@Test
	@Order(value = 0)
	void testUserLogin() {
		{
			Map<String, String> loginDetails = new HashMap<>();
			loginDetails.put("email", EMAIL_ADDRESS);
			loginDetails.put("password", "mypassword");

			Response response = given().contentType(JSON).accept(JSON).body(loginDetails).when()
					.post(CONTEXT_PATH + "/users/login").then().statusCode(200).extract().response();

			authorizationHeader = response.header("Authorization");
			userId = response.header("UserID");

			assertNotNull(authorizationHeader);
			assertNotNull(userId);

		}
	}

	@Test
	@Order(value = 1)
	void testGetUser() {
		{

			Response response = given().pathParam("id", userId).header("Authorization", authorizationHeader)
					.contentType(JSON).accept(JSON).when().get(CONTEXT_PATH + "/users/{id}").then().statusCode(200)
					.contentType(JSON).extract().response();

			String userPublicId = response.jsonPath().getString("userId");
			String userEmail = response.jsonPath().getString("email");
			String firstName = response.jsonPath().getString("firstName");
			String lastName = response.jsonPath().getString("lastName");
			addresses = response.jsonPath().getList("addresses");
			String addressId = addresses.get(0).get("addressId");

			assertNotNull(userPublicId);
			assertNotNull(userEmail);
			assertNotNull(firstName);
			assertNotNull(lastName);
			assertEquals(EMAIL_ADDRESS, userEmail);

			assertTrue(addresses.size() == 2);
			assertTrue(addressId.length() == 30);

		}
	}

	@Test
	@Order(value = 2)
	void testUpdateUser() {

		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put("firstName", "Annet");
		userDetails.put("lastName", "Leonard");

		Response response = given().contentType(JSON).accept(JSON).header("Authorization", authorizationHeader)
				.pathParam("id", userId).accept(JSON).body(userDetails).when().put(CONTEXT_PATH + "/users/{id}").then()
				.statusCode(200).contentType(JSON).extract().response();

		String firstName = response.jsonPath().getString("firstName");
		String lastName = response.jsonPath().getString("lastName");

		assertEquals(userDetails.get("firstName"), firstName);
		assertEquals(userDetails.get("lastName"), lastName);

		// Check that the list of addresses is still there
		List<Map<String, String>> storedAddresses = response.jsonPath().getList("addresses");
		assertNotNull(storedAddresses);
		assertTrue(addresses.size() == storedAddresses.size());
		assertEquals(addresses.get(0).get("streetName"), storedAddresses.get(0).get("streetName"));

	}
	
	@Test
	@Order(value = 3)
	void testDeleteUser() {

		Response response = given().accept(JSON).header("Authorization", authorizationHeader)
				.pathParam("id", userId).accept(JSON)
				.when().delete(CONTEXT_PATH + "/users/{id}")
				.then().statusCode(200).contentType(JSON).extract().response();
		
		String operationResult = response.jsonPath().getString("operationResult");
		String operationName = response.jsonPath().getString("operationName");

		assertEquals("SUCCESS", operationResult);
		assertEquals("DELETE", operationName);

	}

}
