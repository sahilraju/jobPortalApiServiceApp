package com.restapi.jobPortal.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "locations")
public class Location {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(nullable = false)
	private String name;

}
