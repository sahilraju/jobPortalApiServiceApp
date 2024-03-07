package com.restapi.jobPortal.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.jobPortal.auth.JwtUtilInterface;
import com.restapi.jobPortal.dto.Mail;
import com.restapi.jobPortal.dto.ResetPassword;
import com.restapi.jobPortal.dto.User;
import com.restapi.jobPortal.service.UserServiceInterface;

@RestController
public class UserController {
 
	@Autowired
	private UserServiceInterface userService;

	@Autowired
	private JwtUtilInterface jwtUtil;

	@PostMapping("api/user/signin")
	public ResponseEntity<User> signIn(@RequestBody User u) {

		User user = userService.signIn(u);

		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(user);

	}

	@PostMapping("api/user/login")
	public ResponseEntity<String> login(@RequestBody User u) {

		String msge = userService.login(u);

		if (msge == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		jwtUtil.setSECRET_KEY(jwtUtil.generateSecretKeyAsString());

		String generatedToken = jwtUtil.generateToken(u.getEmail()); 

		return ResponseEntity.status(HttpStatus.OK).body(generatedToken); 

	}

	@DeleteMapping("api/user/delete/{email}")
	public ResponseEntity<String> deleteUser(
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
			@PathVariable String email) {

		String token = jwtUtil.extractTokenFromHeader(authorizationHeader);

		if (token == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		boolean isValidToken = jwtUtil.validateToken(token);

		if (!(isValidToken))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		String msge = userService.deleteUser(email);

		if (msge == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email not found");
		}

		return ResponseEntity.status(HttpStatus.OK).body(msge);

	}

	@GetMapping("api/user/forgetpassword")
	public String forgetPassword(@RequestBody Mail mail) {

		String m = userService.forgetPassword(mail);

		return m;
	}

	@PutMapping("api/user/reset")
	public String passwordReset(@RequestBody ResetPassword resetPassword) {

		return userService.passwordReset(resetPassword);

	}

}