package com.restapi.jobPortal.service;

import java.io.IOException;

import java.util.List;

import com.restapi.jobPortal.dto.ApprovedApplicant;
import com.restapi.jobPortal.dto.CreateJob;
import com.restapi.jobPortal.dto.Job;
import com.restapi.jobPortal.dto.JobApplication;

public interface JobServiceInterface {

	public Job createJob(CreateJob cj);

	public Job getJobbyId(Integer id);

	public List<ApprovedApplicant> applyJob(Integer jobId, List<JobApplication> jobApplications)
			throws ClassNotFoundException, IOException;

}
