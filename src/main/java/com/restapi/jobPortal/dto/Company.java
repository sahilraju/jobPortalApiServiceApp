package com.restapi.jobPortal.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "companies")
public class Company {
	
	@Id
	@GeneratedValue
	private Integer id;
	 
	@Column(nullable = false)
	private String name;
	
	@Column(unique = true, nullable = false)
	private String description;
	
}