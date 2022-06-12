package com.samsonmarikwa.appws.service.impl;

import javax.persistence.Column;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsonmarikwa.appws.io.entity.UserEntity;
import com.samsonmarikwa.appws.repository.UserRepository;
import com.samsonmarikwa.appws.service.UserService;
import com.samsonmarikwa.appws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDto createUser(UserDto user) {
		
		// One method to check for existing record to prevent duplication.
		// Another method is to use @Column(nullable=false, unique=true) and let the database throw exception if an attempt is made
		// to insert column data that is not unique
		if(userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Record already exists");
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		userEntity.setEncryptedPassword("test");
		userEntity.setUserId("testUserId");
		
		UserEntity storedUserDetails = userRepository.save(userEntity);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		
		return returnValue;
	}

}
