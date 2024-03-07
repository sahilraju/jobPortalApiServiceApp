package com.restapi.jobPortal.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.restapi.jobPortal.auth.JwtUtilInterface;
import com.restapi.jobPortal.dto.Company;
import com.restapi.jobPortal.service.CompanyServiceInterface;

class CompanyController_Test {

	@Test
	void createCompany() {
		
		class testcase {
			
			String name; 
			String authHeader; 
			Company company;
			Company serArg;
			Company serReturn; 
			String token;
			boolean isTokenValid;
			ResponseEntity<Company> expected;
			
		} 
		 
		testcase t1 = new testcase();
		t1.name = "failing auth header";
		t1.authHeader = null;
		t1.company = null;
		t1.expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		testcase t2 = new testcase();
		t2.name = "failing to extract token";
		t2.authHeader = "12345";
		t2.company = null;
		t2.expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		testcase t3 = new testcase();
		t3.name = "failing while validatiing the token";
		t3.authHeader = "12345";
		t3.company = null;
		t3.token = "wjgecvjqguofboqgq";
		t3.isTokenValid = false;
		t3.expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		
		testcase t4 = new testcase();
		t4.name = "success to create company";
		t4.authHeader = "12345";
		Company c = new Company();
		c.setName("deloitee");
		c.setDescription("top mnc company in USA");
		t4.company = c;
		t4.serArg = c;
		t4.serReturn = c;
		t4.token = "wjgecvjqguofboqgq";
		t4.isTokenValid = true;
		t4.expected = ResponseEntity.status(HttpStatus.OK).body(c); 
		
		List<testcase> testCases = new ArrayList<>();
		
		testCases.add(t1);
		testCases.add(t2); 
		testCases.add(t3); 
		testCases.add(t4);
		
		for(testcase t: testCases) { 
			
			System.out.println(t.name);
			
			JwtUtilInterface jwtMock = mock(JwtUtilInterface.class);
			
			when(jwtMock.extractTokenFromHeader(t.authHeader)).thenReturn(t.token); 
			
			when(jwtMock.validateToken(t.token)).thenReturn(t.isTokenValid);
			
			CompanyServiceInterface csiMock = mock(CompanyServiceInterface.class);
			
			when(csiMock.createCompany(t.serArg)).thenReturn(t.serReturn);
			
			CompanyController cc = new CompanyController(csiMock, jwtMock);
			
		    ResponseEntity<Company> actualResult = cc.createCompany(t.authHeader, t.company); 
		    
		    ResponseEntity<Company> expected = t.expected;
		    
		    assertEquals(expected, actualResult);	
		    
		    System.out.println(t.name+" is ended");
			
		}
		
		//fail("Not yet implemented"); 
	}

}
