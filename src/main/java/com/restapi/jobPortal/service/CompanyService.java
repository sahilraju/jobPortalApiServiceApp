package com.restapi.jobPortal.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.jobPortal.dao.CompanyDao;
import com.restapi.jobPortal.dao.CompanyDaoInterface;
import com.restapi.jobPortal.dto.Company;

@Service
public class CompanyService implements CompanyServiceInterface {

	@Autowired
	private CompanyDaoInterface companyDao;
	
	public CompanyService(CompanyDaoInterface companyDao) {
		this.companyDao = companyDao;
	}
 
	@Override
	public Company createCompany(Company c) {
		
		if(c == null) {
			return null; 
		}
  
		Company c1 = companyDao.createCompany(c);
 
		return c1;  

	}  

	@Override
	public Company getCompanybyId(Integer id) {

		Company c = companyDao.getCompanybyId(id); 
		 
		return c; 	
	
	}
 
	@Override 
	public List<Company> getAllCompanies() { 

		return companyDao.getAllCompanies();
	}

}
