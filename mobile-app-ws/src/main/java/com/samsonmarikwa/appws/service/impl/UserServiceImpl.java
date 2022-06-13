package com.samsonmarikwa.appws.service.impl;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.samsonmarikwa.appws.io.entity.UserEntity;
import com.samsonmarikwa.appws.repository.UserRepository;
import com.samsonmarikwa.appws.service.UserService;
import com.samsonmarikwa.appws.shared.Utils;
import com.samsonmarikwa.appws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto user) {
		
		// One method to check for existing record to prevent duplication.
		// Another method is to use @Column(nullable=false, unique=true) and let the database throw exception if an attempt is made
		// to insert column data that is not unique
		if(userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Record already exists");
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		String generatedUserId = utils.generateUserId(30);
		userEntity.setUserId(generatedUserId);
		// The above may not be necessary as the UUID.randomUUID().toString() generates a random string.
		// Universally unique identifier (UUID) is a 128-bit label used for information in computer systems.
		// The term globally unique identifier (GUID) is also used. Databases can also generate a GUID.
		// userEntity.setUserId(UUID.randomUUID().toString()); 
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		UserEntity storedUserDetails = userRepository.save(userEntity);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if (userEntity == null) throw new UsernameNotFoundException(email);
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

}
