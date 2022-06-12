package com.samsonmarikwa.appws.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.samsonmarikwa.appws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	
	UserDto createUser(UserDto user);

}
