package com.restapi.jobPortal.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Column(unique = true, nullable = false)
	private String email;
	
	@Column(unique = false, nullable = false)
	private String password;
	
	@Column(unique = true, nullable = false)
	private String phone;
		
}
