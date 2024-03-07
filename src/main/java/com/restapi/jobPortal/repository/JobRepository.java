package com.restapi.jobPortal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restapi.jobPortal.dto.Job;


public interface JobRepository extends JpaRepository<Job, Integer> {

}
