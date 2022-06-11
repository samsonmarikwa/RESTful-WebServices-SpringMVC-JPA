package com.samsonmarikwa.appws.service;

import org.springframework.stereotype.Service;

import com.samsonmarikwa.appws.shared.dto.UserDto;

@Service
public interface UserService {
	
	UserDto createUser(UserDto user);

}
