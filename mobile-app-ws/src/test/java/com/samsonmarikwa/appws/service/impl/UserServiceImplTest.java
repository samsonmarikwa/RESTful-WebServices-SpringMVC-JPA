package com.samsonmarikwa.appws.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.samsonmarikwa.appws.io.entity.UserEntity;
import com.samsonmarikwa.appws.repository.UserRepository;
import com.samsonmarikwa.appws.shared.dto.UserDto;

class UserServiceImplTest {
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetUser() {
		
		UserEntity userEntity = new UserEntity();
		// stub - fake object
		userEntity.setId(1L);
		userEntity.setUserId("abcd-efgh");
		userEntity.setFirstName("Samson");
		userEntity.setLastName("Marikwa");
		userEntity.setEmail("sammari4249@gmail.com");
		
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = userService.getUser("test@test.com");
		
		assertNotNull(userDto);
		assertEquals("Samson", userDto.getFirstName());
		assertEquals("Marikwa", userDto.getLastName());
		
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

}
