package com.restapi.jobPortal.dto;

import java.io.Serializable;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "jobs")
public class Job implements Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = "company_id", nullable = false)
	private Integer companyId;

	private String title;

	private Integer salary;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private Integer budget;

	@Column(name = "max_notice_period", nullable = false)
	private Integer minNoticePeriod;

	@Column(name = "min_notice_period", nullable = false)
	private Integer maxNoticePeriod;

	@Column(name = "min_experience", nullable = false)
	private Integer minExperience;

	@Column(name = "max_experience", nullable = false)
	private Integer maxExperience;

	@Column(nullable = false)
	private String role;

	@ManyToMany
	private List<Qualification> qualifications;

	@ManyToMany
	private List<Shift> shifts;

	@ManyToMany
	private List<Location> locations;

	@ManyToMany
	private List<Skill> skills;

	@ManyToMany
	private List<WorkMode> workModes;

}
