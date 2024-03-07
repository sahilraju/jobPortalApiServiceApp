package com.restapi.jobPortal.dao;

import com.restapi.jobPortal.dto.Job;

public interface JobDaoInterface {

	public Job CreatJob(Job j);

	public Job getJobbyId(Integer id);

}
