package com.restapi.jobPortal.dao;

import com.restapi.jobPortal.dto.User;

public interface UserDaoInterface {
	
	public User signIn(User u);
	public User login(String email);
	public boolean deleteUser(String email);
	public User getUserByMail(String mail);
	public String updateUserPassword(String mail, String newPassword);

}
