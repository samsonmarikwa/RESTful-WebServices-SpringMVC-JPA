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
	
	static boolean recordsCreated = false;

	@BeforeEach
	void setUp() throws Exception {
		if (!recordsCreated) {
			createRecords();
		}
	}

	@Test
	void testGetVerifiedUsers() {
		Pageable pageableRequest = PageRequest.of(0, 1);	// retrieves one page - one record 
		Page<UserEntity> page = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
		assertNotNull(page);
		
		List<UserEntity> userEntities = page.getContent();
		assertNotNull(userEntities);
		assertTrue(userEntities.size() == 1);	// should retrieve one record based on the PageRequest
	}
	
	@Test
	void testFindUserByFirstAndLastName() {
		String firstName = "John";
		String lastName = "Doe";
		List<UserEntity> users = userRepository.findUserByFirstNameAndLastName(firstName, lastName);
		assertNotNull(users);
		assertTrue(users.size() == 2);
	}
	
	@Test
	void testFindUserByLastName() {
		String firstName = "John";
		String lastName = "Doe";
		List<UserEntity> users = userRepository.findUserByLastNameFirstName(firstName, lastName);
		assertNotNull(users);
		assertTrue(users.size() == 2);
	}
	
	@Test
	void testFindUserByKeyword() {
		String keyword = "Jo";
		List<UserEntity> users = userRepository.findUsersByKeyword(keyword);
		assertNotNull(users);
		assertTrue(users.size() == 2);
		UserEntity user = users.get(0);
		assertTrue(user.getLastName().contains(keyword) || user.getFirstName().contains(keyword));
	}
	
	@Test
	void testFindUserFirstNameAndLastNameByKeyword() {
		String keyword = "Jo";
		List<Object[]> users = userRepository.findUserFirstNameAndLastNameByKeyword(keyword);
		assertNotNull(users);
		assertTrue(users.size() == 2);
		
		Object[] user = users.get(0);
		String firstname = (String) user[0];
		String lastname = String.valueOf(user[1]);
		
		assertNotNull(firstname);
		assertNotNull(lastname);
	}
	
	@Test
	final void testUpdateUserEmailVerificationStatus() {
		boolean emailVerificationStatus = true;
		String userId = "abcd-efgh-ijkl";
		userRepository.updateUserEmailVerificationStatus(emailVerificationStatus, userId);
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		boolean storedEmailVerificationStatus = userEntity.getEmailVerificationStatus();
		
		assertTrue(storedEmailVerificationStatus == emailVerificationStatus);
	}
	
	@Test
	final void testFindUserEntityByUserId() {
		String userId = "abcd-efgh-ijkl";
		UserEntity userEntity = userRepository.findUserEntityByUserId(userId);
		
		assertNotNull(userEntity);
		assertEquals(userId, userEntity.getUserId());
	}
	
	@Test
	final void testGetUserEntityFullNameById() {
		String userId = "abcd-efgh-ijkl";
		List<Object[]> users = userRepository.getUserEntityFullNameById(userId);
		assertNotNull(users);
		assertTrue(users.size() == 1);
		
		Object[] user = users.get(0);
		String firstname = (String) user[0];
		String lastname = String.valueOf(user[1]);
		
		assertNotNull(firstname);
		assertNotNull(lastname);
	}
	
	@Test
	final void testUpdateUserEntityEmailVerificationStatus() {
		boolean emailVerificationStatus = true;
		String userId = "abcd-efgh-ijkl";
		userRepository.updateUserEntityEmailVerificationStatus(emailVerificationStatus, userId);
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		boolean storedEmailVerificationStatus = userEntity.getEmailVerificationStatus();
		
		assertTrue(storedEmailVerificationStatus == emailVerificationStatus);
	}

	private void createRecords() {
		// Prepare test data
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName("John");
		userEntity.setLastName("Doe");
		userEntity.setUserId("abcd-efgh-ijkl");
		userEntity.setEncryptedPassword("mcjd$5^&8,g");
		userEntity.setEmail("test0@test.com");
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
		
		recordsCreated = true;
	}
	
}


