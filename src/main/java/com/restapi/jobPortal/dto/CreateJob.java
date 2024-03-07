package com.restapi.jobPortal.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateJob {

	private Integer companyId;

	private String title;

	private Integer salary;

	private String description;

	private Integer budget;

	private Integer minNoticePeriod;

	private Integer maxNoticePeriod;

	private Integer minExperience;

	private Integer maxExperience;

	private String role;

	private List<Integer> qualification;

	private List<Integer> shift;

	private List<Integer> location;

	private List<Integer> skill;

	private List<Integer> workMode;

}
