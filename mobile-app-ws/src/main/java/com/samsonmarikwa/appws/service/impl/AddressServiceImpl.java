package com.samsonmarikwa.appws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsonmarikwa.appws.io.entity.AddressEntity;
import com.samsonmarikwa.appws.io.entity.UserEntity;
import com.samsonmarikwa.appws.repository.AddressRepository;
import com.samsonmarikwa.appws.repository.UserRepository;
import com.samsonmarikwa.appws.service.AddressService;
import com.samsonmarikwa.appws.shared.dto.AddressDTO;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;

	@Override
	public List<AddressDTO> getAddresses(String userId) {
		
		List<AddressDTO> returnValue = new ArrayList<>();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) return returnValue;
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		for (AddressEntity addressEntity : addresses) {
			returnValue.add(new ModelMapper().map(addressEntity, AddressDTO.class));
		}
		
		return returnValue;
	}

}
