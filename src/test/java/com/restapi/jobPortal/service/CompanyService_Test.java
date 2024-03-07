package com.restapi.jobPortal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.restapi.jobPortal.dao.CompanyDaoInterface;
import com.restapi.jobPortal.dto.Company;


class CompanyService_Test {


	@Test
	void createCompany() { 

		class testcases {
			String name;
			Company arg;
			Company mockArg;  
			Company mockReturn; 
			Company expected;
		} 

		testcases tc = new testcases();

		tc.name = "success"; 
		Company c = new Company();
		c.setName("deloitee");
		c.setDescription("top mnc company in USA");
		tc.arg = c;
		tc.mockArg = c;
		tc.mockReturn = c;
		tc.expected = c;

		testcases tc2 = new testcases();

		tc2.name = "failure";
		tc2.arg = null;
		tc2.mockArg = null; 
		tc2.mockReturn = null; 
		tc2.expected = null;

		List<testcases> testCaseList = new ArrayList<>();
		 
		testCaseList.add(tc);
		testCaseList.add(tc2);

		for (testcases testcase : testCaseList) {

			System.out.println(testcase.name);
			CompanyDaoInterface csMock = mock(CompanyDaoInterface.class);

			when(csMock.createCompany(testcase.mockArg)).thenReturn(testcase.mockReturn);

			CompanyService cs = new CompanyService(csMock);

			Company actualResult = cs.createCompany(testcase.arg);

			assertEquals(testcase.expected, actualResult);

		}

	}

}
