package com.restapi.jobPortal.dao;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.restapi.jobPortal.dto.Job;
import com.restapi.jobPortal.repository.JobRepository;


@Repository
public class JobDao implements JobDaoInterface {

	@Autowired
	private JobRepository jobRepository;

	@Override
	public Job CreatJob(Job j) {

		return jobRepository.save(j);

	}
    
	@Override
	public Job getJobbyId(Integer id) {

		Optional<Job> opt = jobRepository.findById(id);

		return opt.get();
 
	}

}
