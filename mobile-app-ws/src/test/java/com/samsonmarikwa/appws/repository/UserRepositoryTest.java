package com.samsonmarikwa.appws.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.samsonmarikwa.appws.io.entity.AddressEntity;
import com.samsonmarikwa.appws.io.entity.UserEntity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {
	
	@Autowired
	UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		// Prepare test data
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName("John");
		userEntity.setLastName("Doe");
		userEntity.setUserId("abcd-efgh-ijkl");
		userEntity.setEncryptedPassword("mcjd$5^&8,g");
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationStatus(true);
		
		AddressEntity addressEntity = new AddressEntity();
		addressEntity.setType("shipping");
		addressEntity.setAddressId("xyz-abc-plm");
		addressEntity.setCity("Vancouver");
		addressEntity.setCountry("Canada");
		addressEntity.setPostalCode("12345");
		addressEntity.setStreetName("123 Street Address");
		
		List<AddressEntity> addresses = new ArrayList<>();
		addresses.add(addressEntity);
		
		userEntity.setAddresses(addresses);
		
		userRepository.save(userEntity);
		
		UserEntity userEntity2 = new UserEntity();
		userEntity2.setFirstName("John");
		userEntity2.setLastName("Doe");
		userEntity2.setUserId("zxyx-efgh-ijkl");
		userEntity2.setEncryptedPassword("mcjd$5^&8,g");
		userEntity2.setEmail("test1@test.com");
		userEntity2.setEmailVerificationStatus(true);
		
		AddressEntity addressEntity2 = new AddressEntity();
		addressEntity2.setType("shipping");
		addressEntity2.setAddressId("xyz-abc-plmy");
		addressEntity2.setCity("Vancouver");
		addressEntity2.setCountry("Canada");
		addressEntity2.setPostalCode("12345");
		addressEntity2.setStreetName("123 Street Address");
		
		List<AddressEntity> addresses2 = new ArrayList<>();
		addresses2.add(addressEntity2);
		
		userEntity2.setAddresses(addresses2);
		
		userRepository.save(userEntity2);
	}

	@Test
	void testGetVerifiedUsers() {
		Pageable pageableRequest = PageRequest.of(0, 1);	// retrieves one page - one record 
		Page<UserEntity> page = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
		assertNotNull(page);
		
		List<UserEntity> userEntities = page.getContent();
		assertNotNull(userEntities);
		assertTrue(userEntities.size() == 1);	// should retrieve one record
	}
	
}


