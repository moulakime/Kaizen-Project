package com.kaizen.project.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.kaizen.project.model.User;

public interface MyUserDetails extends UserDetailsService {

	User findByEmail(String email);
	
}
