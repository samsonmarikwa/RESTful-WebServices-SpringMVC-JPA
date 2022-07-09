package com.samsonmarikwa.appws.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsonmarikwa.appws.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

	UserEntity findByEmail(String email);

	UserEntity findByUserId(String userId);

	UserEntity findUserByEmailVerificationToken(String token);

	// countQuery is required only if we have a Pageable Request so SpringJPA can
	// figure how to split the number of records to be returned per page.
	// If not supplied, JPA will try to figure out the count records by itself.
	@Query(value = "select * from Users", countQuery = "select count(*) from Users", nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);

	// positional or index parameters - the order should match
	@Query(value = "select * from Users u where u.first_name = ?1 and u.last_name = ?2", nativeQuery = true)
	List<UserEntity> findUserByFirstNameAndLastName(String firstName, String lastName);

	// named parameters- should match @Param with the param in the query. The order
	// matching is not required
	@Query(value = "select * from Users u where u.last_name = :lastname and u.first_name = :firstname", nativeQuery = true)
	List<UserEntity> findUserByLastNameFirstName(@Param("firstname") String firstName,
			@Param("lastname") String lastName);

	// Search for any firstname that starts with any characters but ends with the specified keyword %:keyword
	// Search for any firstname that has the specified keyword as part of its name %:keyword%
	// Search for any firstname that has starts with the specified keyword :keyword%
	// matching is not required
	@Query(value = "select * from Users u where u.first_name LIKE %:keyword% or u.last_name LIKE %:keyword%", nativeQuery = true)
	List<UserEntity> findUsersByKeyword(@Param("keyword") String keyword);
	
	// Select specific column names from a table
	@Query(value = "select u.first_name, u.last_name from Users u where u.first_name LIKE %:keyword% or u.last_name LIKE %:keyword%", nativeQuery = true)
	List<Object[]> findUserFirstNameAndLastNameByKeyword(@Param("keyword") String keyword);
	
	@Transactional	// if errors take place, then a rollback takes place and any partial updates will be removed. Usually, this annotation is put on a service method class that calls the repository method
	@Modifying // This JPA annotation is used for mutation queries
	@Query(value="update users u set u.EMAIL_VERIFICATION_STATUS=:emailVerificationStatus WHERE u.user_id = :userId", nativeQuery = true)
	void updateUserEmailVerificationStatus(
			@Param("emailVerificationStatus") boolean emailVerificationStatus,
			@Param("userId") String userId);
	
	// JPQL - Java Persistence Query Language - no value and nativeQuery attributes
	@Query("select usr from UserEntity usr where usr.userId = :userId")
	UserEntity findUserEntityByUserId(@Param("userId") String userId);
	
	@Query("select user.firstName, user.lastName from UserEntity user where user.userId = :userId")
	List<Object[]> getUserEntityFullNameById(@Param("userId") String userId);
	
	@Transactional
	@Modifying
	@Query("update UserEntity u set u.emailVerificationStatus=:emailVerificationStatus WHERE u.userId = :userId")
	void updateUserEntityEmailVerificationStatus(
			@Param("emailVerificationStatus") boolean emailVerificationStatus,
			@Param("userId") String userId);
	
}
