package com.restapi.jobPortal.dto;

import lombok.Data;

@Data
public class ResetPassword {
	
	private String gmail;
	private String otp;
	private String password;
	private String confirmpassword;

}
 
