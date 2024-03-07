package com.restapi.jobPortal.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.restapi.jobPortal.dto.User;
import com.restapi.jobPortal.repository.UserRepository;

@Repository
public class UserDao implements UserDaoInterface {

	@Autowired
	private UserRepository userRepo;

	@Override
	public User signIn(User u) {

		return userRepo.save(u);
	}

	@Override
	public User login(String email) {

		return userRepo.getUserByEmail(email);

	}
    @Override
	public boolean deleteUser(String email) {

		User u = userRepo.getUserByEmail(email);

		if (u != null) {

			Optional<User> opt = userRepo.findById(u.getId());

			if (opt.isPresent()) {
				User u1 = opt.get();
				userRepo.delete(u1);
				return true;
			}

		}
		return false;
	}
    @Override
	public User getUserByMail(String mail) {

		return userRepo.getUserByEmail(mail);

	}
    @Override
	public String updateUserPassword(String mail, String newPassword) {

		User user = getUserByMail(mail);
		
		if (user == null) {
			return "invalid gmail";
		}
		 
		Optional<User> opt = userRepo.findById(user.getId());
	
		if(opt.isPresent()) {
			User user1 = opt.get();
			user1.setPassword(newPassword);
			userRepo.save(user1);
			return "password successfully reset";
		}
		return "gmail not found to update";
	}

}
