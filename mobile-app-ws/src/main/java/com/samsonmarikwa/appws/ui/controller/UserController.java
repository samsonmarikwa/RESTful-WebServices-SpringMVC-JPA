package com.samsonmarikwa.appws.ui.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsonmarikwa.appws.exceptions.UserServiceException;
import com.samsonmarikwa.appws.service.AddressService;
import com.samsonmarikwa.appws.service.UserService;
import com.samsonmarikwa.appws.shared.dto.AddressDTO;
import com.samsonmarikwa.appws.shared.dto.UserDto;
import com.samsonmarikwa.appws.ui.model.request.UserDetailsRequestModel;
import com.samsonmarikwa.appws.ui.model.response.AddressRest;
import com.samsonmarikwa.appws.ui.model.response.ErrorMessages;
import com.samsonmarikwa.appws.ui.model.response.OperationStatusModel;
import com.samsonmarikwa.appws.ui.model.response.RequestOperationName;
import com.samsonmarikwa.appws.ui.model.response.RequestOperationStatus;
import com.samsonmarikwa.appws.ui.model.response.UserRest;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;

	// If client does not send Accept header value, then the first definition in the
	// produces is taken as the return type.
	// In this case, XML will be the default response type. To get JSON, the Accept
	// header value should be application/json
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String id) {

		UserDto userDto = userService.getUserByUserId(id);
//		BeanUtils.copyProperties(userDto, returnValue);
		ModelMapper modelMapper = new ModelMapper();
		UserRest returnValue = modelMapper.map(userDto, UserRest.class);

		return returnValue;
	}

	@PostMapping(
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

		UserRest returnValue = new UserRest();

		if (userDetails.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		if (userDetails.getLastName().isEmpty())
			throw new NullPointerException("Lastname cannot be empty");

//		UserDto userDto = new UserDto();
//		BeanUtils.copyProperties(userDetails, userDto);
		
		// ModelMapper works similarly to BeanUtils. ModelMapper is able to handle nested objects.
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		
		UserDto createdUser = userService.createUser(userDto);
//		BeanUtils.copyProperties(createdUser, returnValue);
		returnValue = modelMapper.map(createdUser, UserRest.class);

		return returnValue;
	}

	@PutMapping(path = "/{id}",
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {

		UserRest returnValue = new UserRest();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updatedUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);
		
		return returnValue;
	}

	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUser(id);
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

		return returnValue;
	}
	
	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="limit", defaultValue="25") int limit) {
		
		List<UserRest> returnValue = new ArrayList<>();
		
		List<UserDto> users = userService.getUsers(page, limit);
		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		
		return returnValue;
	}
	
	@GetMapping(path="/{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<AddressRest> getUserAddresses(@PathVariable String id) {
		
		List<AddressRest> returnValue = new ArrayList<>();
		
		List<AddressDTO> addressDto = addressService.getAddresses(id);
		
		if (addressDto != null && !addressDto.isEmpty()) {
			java.lang.reflect.Type listType = new TypeToken<List<AddressRest>>() {}.getType();
			returnValue = new ModelMapper().map(addressDto, listType);
		}
		
		return returnValue;
			
	}
	
	@GetMapping(path="/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public EntityModel<AddressRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
//		public AddressRest getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
		
		AddressDTO addressDto = addressService.getAddress(addressId);
		AddressRest returnValue = new ModelMapper().map(addressDto, AddressRest.class);
		
		// Implement HATEOAS - Hypertext As The Engine Of Application State
		// http://localhost:8080/users/<userId>
		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
		// http://localhost:8080/users/<userId>/addresses
		Link userAddressesLink = WebMvcLinkBuilder.linkTo(UserController.class)
				.slash(userId)
				.slash("addresses")
				.withRel("addresses");
		// http://localhost:8080/users/<userId>/addresses/<addressId>
		Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class)
				.slash(userId)
				.slash("addresses")
				.slash(addressId)
				.withSelfRel();
		
//		Not required, since we are now using EntityModel
//		returnValue.add(userLink);
//		returnValue.add(userAddressesLink);
//		returnValue.add(selfLink);
		
		return EntityModel.of(returnValue, Arrays.asList(userLink, userAddressesLink, selfLink));
				
//		return returnValue;
			
	}

}
