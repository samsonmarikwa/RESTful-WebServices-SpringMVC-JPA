package com.samsonmarikwa.appws.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsonmarikwa.appws.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	
	UserEntity findByEmail(String email);

	UserEntity findByUserId(String userId);

	UserEntity findUserByEmailVerificationToken(String token);
	
	// countQuery is required only if we have a Pageable Request so SpringJPA can figure how to split the number of records to be returned per page.
	// If not supplied, JPA will try to figure out the count records by itself.
	@Query(value="select * from Users",
			countQuery="select count(*) from Users",
			nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);
	
	// positional or index parameters - the order should match
	@Query(value="select * from Users u where u.first_name = ?1 and u.last_name = ?2",
			nativeQuery = true)
	List<UserEntity> findUserByFirstNameAndLastName(String firstName, String lastName);
	
	// named parameters- should match @Param with the param in the query. The order matching is not required
	@Query(value="select * from Users u where u.last_name = :lastname",
			nativeQuery = true)
	List<UserEntity> findUserByLastName(@Param("lastname") String lastName);

}
