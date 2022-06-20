package com.samsonmarikwa.appws.service;

import java.util.List;

import com.samsonmarikwa.appws.shared.dto.AddressDTO;

public interface AddressService {
	
	List<AddressDTO> getAddresses(String userId);

}
