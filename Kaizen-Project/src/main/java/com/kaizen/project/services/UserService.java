package com.kaizen.project.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kaizen.project.model.Role;
import com.kaizen.project.model.User;
import com.kaizen.project.repository.UserRepository;
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public void createUser(User user)
	{
		Date date = new Date();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		user.setCompany_name("Lear Corporation");
		user.setCountry("Morocco");
		user.setTown("Rabat");
		user.setCreated_at(date);
		user.setUpdated_at(date);
		user.setAvatar("default.jpg");
		user.setRoles(Arrays.asList(new Role("ROLE_USER")));
		userRepository.save(user);
	}
	public void createAdmin(User user)
	{
		Date date = new Date();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		user.setCompany_name("Lear Corporation");
		user.setCountry("Morocco");
		user.setTown("Rabat");
		user.setCreated_at(date);
		user.setUpdated_at(date);
		user.setAvatar("default.jpg");
		user.setRoles(Arrays.asList(new Role("ROLE_ADMIN")));
		userRepository.save(user);
	}
	
	public void updateUser(User user) {
		Date date = new Date();
		User userToUpdate = userRepository.findByEmail(user.getEmail());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		userToUpdate.setPassword(encoder.encode(user.getPassword()));
		userToUpdate.setUpdated_at(date);
		/*userToUpdate.setAvatar("default.jpg");*/
		userToUpdate.setFirst_name(user.getFirst_name());
		userToUpdate.setLast_name(user.getLast_name());
		userToUpdate.setDepartment(user.getDepartment());
		userToUpdate.setJob_title(user.getJob_title());
		userToUpdate.setTown(user.getTown());
		userToUpdate.setCountry(user.getCountry());
		userToUpdate.setSupervisor(user.getSupervisor());
		userToUpdate.setCompany_name("Lear Corporation");
	
		userRepository.save(userToUpdate);
	}
	public void deleteUser(User user) {

		User userDelete = userRepository.findById(user.getId()).orElse(null);
		userRepository.delete(userDelete);
		
	}
	
	public void upgradeUser(User user) {
		User userToUpgrade = userRepository.findByImmatricule(user.getImmatricule());
		userToUpgrade.setRoles(user.getRoles());
		userRepository.save(userToUpgrade);
	}
	
	
}
