package com.restapi.jobPortal.service;

import java.util.List;


import com.restapi.jobPortal.dto.Company;

public interface CompanyServiceInterface {

	public Company createCompany(Company c);

	public Company getCompanybyId(Integer id);
	
	public List<Company> getAllCompanies();

}
  