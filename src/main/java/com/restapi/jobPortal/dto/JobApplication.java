package com.restapi.jobPortal.dto;

import java.util.List;

import lombok.Data;

@Data
public class JobApplication {
	
	private String name;
	private String email;
	private String phone;
	private Integer noticePeriod;
	private Integer experience;
	private Integer expectedSalary;
	private List<Integer> qualification;
	private List<Integer> shift;
	private List<Integer> location;
	private List<Integer> skill;
	private List<Integer> workMode;
	
}
