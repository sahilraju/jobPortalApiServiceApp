package com.restapi.jobPortal.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.jobPortal.auth.JwtUtil;
import com.restapi.jobPortal.auth.JwtUtilInterface;
import com.restapi.jobPortal.dto.Company;
import com.restapi.jobPortal.service.CompanyService;
import com.restapi.jobPortal.service.CompanyServiceInterface;
import com.restapi.logger.LoggerAspect;

@RestController
public class CompanyController {
  
	@Autowired
	private CompanyServiceInterface companyService; 
	
	@Autowired 
	private JwtUtilInterface jwtUtil; 
	
	public CompanyController(CompanyServiceInterface companyService, JwtUtilInterface jwtUtil) {
		this.companyService = companyService;
		this.jwtUtil = jwtUtil;
	}   

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAspect.class);

	@PostMapping("api/company/create") 
	public ResponseEntity<Company> createCompany( 
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
			@RequestBody Company c) {
		
		if(authorizationHeader == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
  
		String token = jwtUtil.extractTokenFromHeader(authorizationHeader);
 
		if (token == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		boolean isValidToken = jwtUtil.validateToken(token);

		if (!(isValidToken))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		Company company = companyService.createCompany(c);

		return ResponseEntity.status(HttpStatus.OK).body(company); 

	}

	@GetMapping("api/company/{id}")
	public ResponseEntity<Company> getCompanybyId(
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
			@PathVariable Integer id) {
		
		if(authorizationHeader == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		} 

		String token = jwtUtil.extractTokenFromHeader(authorizationHeader);

		if (token == null) {
			LOGGER.error("token not recived ///////////////////");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		boolean isValidToken = jwtUtil.validateToken(token);

		if (!(isValidToken))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		Company c = companyService.getCompanybyId(id);

		if (c == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} 

		return ResponseEntity.status(HttpStatus.OK).body(c);
	}

	@GetMapping("api/companies")
	public ResponseEntity<List<Company>> getAllCompanies(
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

		String token = jwtUtil.extractTokenFromHeader(authorizationHeader);

		if (token == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		boolean isValidToken = jwtUtil.validateToken(token);

		if (!(isValidToken))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		List<Company> companies = companyService.getAllCompanies();
 
		if (companies == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(companies);

	}

}
