package com.restapi.jobPortal.dao;

import java.util.List;

import com.restapi.jobPortal.dto.Company;

public interface CompanyDaoInterface {

	public Company createCompany(Company c);

	public Company getCompanybyId(Integer id);
	
	public List<Company> getAllCompanies();

}
