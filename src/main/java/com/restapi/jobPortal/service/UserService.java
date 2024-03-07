package com.restapi.jobPortal.service;

import java.security.MessageDigest;


import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.restapi.jobPortal.auth.JwtUtil;
import com.restapi.jobPortal.dao.UserDao;
import com.restapi.jobPortal.dto.Mail;
import com.restapi.jobPortal.dto.ResetPassword;
import com.restapi.jobPortal.dto.User;

import redis.clients.jedis.Jedis;

@Service
public class UserService implements UserServiceInterface {

	@Autowired
	private UserDao userDao;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String fromMail;

	private Jedis jedis = new Jedis("127.0.0.1", 6379);
	
	@Override 
	public User signIn(User u) {
 
		if (u == null) { 
			return null;
		}
		if (verifyCredentials(u.getEmail(), "[A-Za-z].{5,25}gmail.com") == false) {
			return null; 
		}
		if (verifyCredentials(u.getPhone(), "[6-9][0-9]{9}") == false) {
			return null; 
		}

		String hashPassword = doHashing(u.getPassword());

		u.setPassword(hashPassword);

		User u1 = userDao.signIn(u);

		return u1; 

	}
 
	@Override
	public String login(User u) {  
 
		if (u == null) { 
			return null; 
		}
		if (!(verifyCredentials(u.getEmail(), "[A-Za-z].{5,25}gmail.com"))) {
			return null;
		}

		User u1 = userDao.login(u.getEmail());

		if (u1 == null) {
			return null; 
		}

		if (!(doHashing(u.getPassword()).equals(u1.getPassword()))) { 
			return null;
		}
		
//		JwtUtil.setSECRET_KEY(JwtUtil.generateSecretKeyAsString());
//		
//		String generatedToken = JwtUtil.generateToken(u1.getEmail());
		
		return "data inserted";
  
	}
 
	@Override
	public String forgetPassword(Mail mail) {

		User user = userDao.getUserByMail(mail.getGmail());

		if (user == null) {
			return "gmail not found to send otp";
		}

		String otp = otpGenerate();

		try {

			jedis.setex(mail.getGmail(), 300, otp);
			System.out.println(jedis.get(mail.getGmail()) + "data cached successfully");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}

		sendMail(javaMailSender, fromMail, "PASSWORD RESET", "OTP for reset password: " + otp, mail.getGmail());

		return mail.getGmail(); 

	}
 
	@Override
	public String passwordReset(ResetPassword resetPassword) {

		User user = userDao.getUserByMail(resetPassword.getGmail());

		if (user == null) {

			return "invalid gmail";

		}

		if (!(resetPassword.getOtp().equals(jedis.get(resetPassword.getGmail())))) {

			return "invalid otp provided";

		}

		if (!(resetPassword.getPassword().equals(resetPassword.getConfirmpassword()))) {

			return "password and confirm password does not matched";

		}

		return userDao.updateUserPassword(resetPassword.getGmail(), doHashing(resetPassword.getPassword()));

	}

	@Override
	public String deleteUser(String email) {

		if (email == null || verifyCredentials(email, "[A-Za-z].{5,25}gmail.com") == false) {
			return null;
		} 

		if (userDao.deleteUser(email) == false) {
			return null;
		}
  
		return "user deleted successfully"; 
	}

	public static boolean verifyCredentials(String value, String exp) {

		Pattern p = Pattern.compile(exp);
		Matcher m = p.matcher(value);

		if (m.matches())
			return true;
		else
			return false;
	}

	public static String doHashing(String password) {

		try {

			MessageDigest messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.update(password.getBytes());

			byte[] resultByteArray = messageDigest.digest();

			StringBuilder sb = new StringBuilder();

			for (byte b : resultByteArray) {
				sb.append(String.format("%02x", b));
			}

			return sb.toString();

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}

		return "";

	}

	public static String otpGenerate() {

		Random random = new Random();

		// Generate a random 6-digit number
		int min = 100000; // Minimum value for a 6-digit number
		int max = 999999; // Maximum value for a 6-digit number
		int randomNumber = random.nextInt(max - min + 1) + min;

		System.out.println(randomNumber + "||||||||||||||||||||||");

		return randomNumber + "";
	}

	public void sendMail(JavaMailSender javaMailSender, String fromMail, String subject, String body, String toMail) {

		SimpleMailMessage smm = new SimpleMailMessage();

		smm.setFrom(fromMail);
		smm.setSubject(subject);
		smm.setText(body);
		smm.setTo(toMail);

		javaMailSender.send(smm);

		System.out.println("Mail sent successfully|||||||||||||||||||||");

	}

}
