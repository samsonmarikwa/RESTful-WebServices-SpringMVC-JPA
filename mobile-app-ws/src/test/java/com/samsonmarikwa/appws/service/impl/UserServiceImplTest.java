package com.samsonmarikwa.appws.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.samsonmarikwa.appws.exceptions.UserServiceException;
import com.samsonmarikwa.appws.io.entity.AddressEntity;
import com.samsonmarikwa.appws.io.entity.UserEntity;
import com.samsonmarikwa.appws.repository.UserRepository;
import com.samsonmarikwa.appws.shared.AmazonSES;
import com.samsonmarikwa.appws.shared.Utils;
import com.samsonmarikwa.appws.shared.dto.AddressDTO;
import com.samsonmarikwa.appws.shared.dto.UserDto;

class UserServiceImplTest {
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	Utils utils;
	
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock
	AmazonSES amazonSES;
		
	UserEntity userEntity;
	
	String userId = "abcd-efgh";
	String encryptedPassword = "xkjnjgogngkggf895kkk$^";

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setUserId(userId);
		userEntity.setFirstName("Don");
		userEntity.setLastName("Quixote");
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setEmail("samsonmarikwa@outlook.com");
		userEntity.setEmailVerificationToken("dfjkjjgjgnjg");
		userEntity.setAddresses(getAddressesEntities());
	}

	@Test
	void testGetUser() {
		
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = userService.getUser("test@test.com");
		
		assertNotNull(userDto);
		assertEquals("Don", userDto.getFirstName());
		assertEquals("Quixote", userDto.getLastName());
		
	}
	
	@Test
	void testGetUser_UsernameNotFoundException () {
		
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		
		// We expect the Lambda exp to throw a UsernameNotFoundException.
		// If it does not throw, then the test should fail
		assertThrows(UsernameNotFoundException.class, () -> {
			userService.getUser("test@test.com");
		});
		
	}
	
	@Test
	void testCreateUser_UserServiceException () {
		
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = new UserDto();
		userDto.setId(1L);
		userDto.setUserId(userId);
		userDto.setFirstName("Don");
		userDto.setLastName("Quixote");
		userDto.setPassword("password");
		userDto.setEmail("samsonmarikwa@outlook.com");
		userDto.setAddresses(getAddressesDto());
		
		assertThrows(UserServiceException.class, () -> {
			userService.createUser(userDto);
		});
		
	}
	
	@Test
	void testCreateUser() {
		
		// This class under test requires to be broken into chunks to make unit testing easier.
		
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(utils.generateEmailVerificationToken(anyString())).thenReturn("verificationtoken");
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		// The AmazonSES class is calling an Amazon service, so we should not test it within this class.
		// as this becomes an integration test, so we are doing nothing in this test
		Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));
		
		UserDto userDto = new UserDto();
		userDto.setId(1L);
		userDto.setUserId(userId);
		userDto.setFirstName("Don");
		userDto.setLastName("Quixote");
		userDto.setPassword("password");
		userDto.setEmail("samsonmarikwa@outlook.com");
		userDto.setAddresses(getAddressesDto());
		
		UserDto createUser = userService.createUser(userDto);
		
		assertNotNull(createUser);
		assertEquals(userEntity.getFirstName(), createUser.getFirstName());
		assertEquals(userEntity.getLastName(), createUser.getLastName());
		assertEquals(userEntity.getAddresses().size(), createUser.getAddresses().size());
		assertNotNull(createUser.getUserId());
		verify(utils, times(createUser.getAddresses().size())).generateAddressId(30);
		verify(bCryptPasswordEncoder, times(1)).encode("password");
		verify(userRepository, times(1)).save(any(UserEntity.class));
		
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
