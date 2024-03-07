package com.restapi.jobPortal.service;

import com.restapi.jobPortal.dto.Mail;

import com.restapi.jobPortal.dto.ResetPassword;
import com.restapi.jobPortal.dto.User;

public interface UserServiceInterface {

	public User signIn(User u);

	public String login(User u);

	public String forgetPassword(Mail mail);

	public String passwordReset(ResetPassword resetPassword);

	public String deleteUser(String email);

}
   