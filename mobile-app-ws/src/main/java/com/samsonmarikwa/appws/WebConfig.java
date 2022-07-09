package com.samsonmarikwa.appws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/users/email-verification") // for a specific endpoints
		 // for all the endpoints
		registry
			.addMapping("/**")
			.allowedMethods("*")
			.allowedOrigins("*");
	}

}
