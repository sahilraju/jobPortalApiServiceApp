package com.restapi.jobPortal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restapi.jobPortal.dto.User;


public interface UserRepository extends JpaRepository<User, Integer> {
	
	public User getUserByEmail(String email);

}
