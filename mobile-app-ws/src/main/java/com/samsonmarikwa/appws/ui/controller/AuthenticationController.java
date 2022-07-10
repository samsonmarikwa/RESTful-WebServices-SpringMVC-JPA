package com.samsonmarikwa.appws.ui.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.samsonmarikwa.appws.ui.model.request.LoginRequestModel;

import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ResponseHeader;

@RestController
public class AuthenticationController {
	
	@ApiOperation("User Login")
	@ApiResponses(value = {
			@ApiResponse(code = 200,
					message = "Response Headers",
					responseHeaders = {
							@ResponseHeader(name = "authorization",
									description = "Bearer <JWT value here>",
									response = String.class),
							@ResponseHeader(name = "userId",
									description = "Public User Id value here",
									response = String.class),
					}
			)
	})
	@PostMapping(path="/users/login",
	produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
	consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public void theFakeLogin(@RequestBody LoginRequestModel loginRequestModel) {
		throw new IllegalStateException("This method should not be called. The method is implemented by Spring Security");
	}
}
