package com.restapi.logger;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAspect.class);

	@Before("public * com.restapi.jobPortal.controller.CompanyController.getCompanybyId(\r\n"
			+ "			@RequestHeader(value = \"Authorization\", required = false) String authorizationHeader,\r\n"
			+ "			@PathVariable Integer id)")
	public void logBefore() {
		
		LOGGER.info("getCompanybyId started executing////////////");

	}

}
