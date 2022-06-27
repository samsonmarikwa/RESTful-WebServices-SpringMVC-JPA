package com.samsonmarikwa.appws.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import com.samsonmarikwa.appws.io.entity.AddressEntity;
import com.samsonmarikwa.appws.service.UserService;
import com.samsonmarikwa.appws.shared.dto.AddressDTO;
import com.samsonmarikwa.appws.shared.dto.UserDto;
import com.samsonmarikwa.appws.ui.model.response.UserRest;

class UserControllerTest {
	
	private static final String USER_ID = "abcd-efgh-wxyz";

	@InjectMocks
	UserController userController;	// class under test
	
	@Mock
	UserService userService;
	
	UserDto userDto;	

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);	// the UserController is autowired and the userService is injected.
		userDto = new UserDto();
		userDto.setUserId(USER_ID);
		userDto.setFirstName("Samson");
		userDto.setLastName("Marikwa");
		userDto.setEmail("test@tes.com");
		userDto.setEncryptedPassword("encryptedPassword");
		userDto.setEmailVerificationToken(null);
		userDto.setEmailVerificationStatus(Boolean.FALSE);
		userDto.setAddresses(getAddressesDto());
		
	}

	@Test
	void testGetUser() {
		
		// Arrange
		when(userService.getUserByUserId(anyString())).thenReturn(userDto);
		
		// Act
		UserRest userRest = userController.getUser(USER_ID);
		
		// Assert
		assertNotNull(userRest);
		assertEquals(USER_ID, userRest.getUserId());
		assertEquals(userDto.getFirstName(), userRest.getFirstName());
		assertEquals(userDto.getLastName(), userRest.getLastName());
		assertEquals(userDto.getEmail(), userRest.getEmail());
		assertEquals(userDto.getAddresses().size(), userRest.getAddresses().size());
		assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());
		
	}
	
	private List<AddressDTO> getAddressesDto() {
		AddressDTO addressDto1 = new AddressDTO();
		addressDto1.setType("billing");
		addressDto1.setCity("Charlotte");
		addressDto1.setCountry("USA");
		addressDto1.setPostalCode("ABC123");
		addressDto1.setStreetName("123 Street Name");
		
		AddressDTO addressDto2 = new AddressDTO();
		addressDto2.setType("shipping");
		addressDto2.setCity("Charlotte");
		addressDto2.setCountry("USA");
		addressDto2.setPostalCode("ABC123");
		addressDto2.setStreetName("123 Street Name");
		
		List<AddressDTO> addresses = new ArrayList<>();
		addresses.add(addressDto1);
		addresses.add(addressDto2);
		
		return addresses;
	}
	
	private List<AddressEntity> getAddressesEntities() {
		List<AddressDTO> addresses = getAddressesDto();
		
		Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
		
		return new ModelMapper().map(addresses, listType);
	}


}
