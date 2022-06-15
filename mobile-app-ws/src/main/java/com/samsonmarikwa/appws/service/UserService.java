package com.samsonmarikwa.appws.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.samsonmarikwa.appws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	
	UserDto createUser(UserDto user);
	UserDto getUser(String email);
	UserDto getUserByUserId(String userId);
	UserDto updateUser(String id, UserDto userDto);
	void deleteUser(String id);

}
