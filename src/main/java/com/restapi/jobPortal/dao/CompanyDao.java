package com.restapi.jobPortal.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.restapi.jobPortal.dto.Company;
import com.restapi.jobPortal.repository.CompanyRepository;

@Repository
public class CompanyDao implements CompanyDaoInterface {

	@Autowired
	private CompanyRepository companyRepo;

	@Override
	public Company createCompany(Company c) { 

		return companyRepo.save(c); 
	}

	@Override
	public Company getCompanybyId(Integer id) {

		Optional<Company> opt = companyRepo.findById(id);

		return opt.get();
	}
	 
	@Override
	public List<Company> getAllCompanies() {
		
		return companyRepo.findAll();
		
	}

}
 