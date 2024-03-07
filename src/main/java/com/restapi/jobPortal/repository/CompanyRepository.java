package com.restapi.jobPortal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restapi.jobPortal.dto.Company;


public interface CompanyRepository extends JpaRepository<Company, Integer>{

}
