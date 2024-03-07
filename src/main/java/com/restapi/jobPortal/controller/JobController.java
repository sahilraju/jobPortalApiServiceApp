package com.restapi.jobPortal.controller;

import java.io.IOException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.jobPortal.auth.JwtUtil;
import com.restapi.jobPortal.auth.JwtUtilInterface;
import com.restapi.jobPortal.dto.ApprovedApplicant;
import com.restapi.jobPortal.dto.CreateJob;
import com.restapi.jobPortal.dto.Job;
import com.restapi.jobPortal.dto.JobApplication;
import com.restapi.jobPortal.service.JobService;
import com.restapi.jobPortal.service.JobServiceInterface;

@RestController
public class JobController {

	@Autowired
	private JobServiceInterface jobService; 
	
	@Autowired(required = true) 
	private JwtUtilInterface jwtUtil; 

	@PostMapping("api/job/post")
	public ResponseEntity<String> createJob(
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
			@RequestBody CreateJob cj) {
		String token = jwtUtil.extractTokenFromHeader(authorizationHeader);

		if (token == null) 
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		boolean isValidToken = jwtUtil.validateToken(token);

		if (!(isValidToken))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		Job job = jobService.createJob(cj);

		if (job == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body("job created successfully");

	}

	@GetMapping("api/job/{id}")
	public ResponseEntity<Job> getJobbyId(
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
			@PathVariable Integer id) {

		String token = jwtUtil.extractTokenFromHeader(authorizationHeader);

		if (token == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		boolean isValidToken = jwtUtil.validateToken(token);

		if (!(isValidToken))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		Job job = jobService.getJobbyId(id);

		if (job == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(job);

	}

	@GetMapping("api/job/apply/{jobId}")
	public ResponseEntity<List<ApprovedApplicant>> applyJob(
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
			@PathVariable Integer jobId, @RequestBody List<JobApplication> jobApplication) {
		
		String token = jwtUtil.extractTokenFromHeader(authorizationHeader);

		if (token == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		boolean isValidToken = jwtUtil.validateToken(token);

		if (!(isValidToken))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);


		try { 
			return ResponseEntity.status(HttpStatus.OK).body(jobService.applyJob(jobId, jobApplication));
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) { 
			System.out.println(e);
		}
		return null;

	}

}
