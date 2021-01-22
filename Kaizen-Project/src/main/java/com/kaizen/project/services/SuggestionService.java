package com.kaizen.project.services;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.kaizen.project.model.Department;
import com.kaizen.project.model.Suggestion;
import com.kaizen.project.model.Type;
import com.kaizen.project.model.User;
import com.kaizen.project.repository.DepartmentRepository;
import com.kaizen.project.repository.SuggestionRepository;
import com.kaizen.project.repository.UserRepository;

@Service
public class SuggestionService {

	@Autowired
	private SuggestionRepository suggestionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DepartmentService departmentService;
	
	public void createSuggestion(Suggestion suggestion)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
	    User Customuser= userRepository.findByEmail(userDetails.getUsername());
	    
		Date date = new Date();
		suggestion.setCreated_at(date);
		suggestion.setChoices("Pending");
		suggestion.setUpdated_at(null);
		suggestion.setJustification("");
		suggestion.setAdopted_solution("");
		suggestion.setComment("");
		//suggestion.setAttachement_file(null);
		suggestion.setUser_id(Customuser);
		suggestionRepository.save(suggestion);
	}	
	
	public void updateSuggestion(Suggestion suggestion) {
		Date date = new Date();
		Suggestion SuggestionToUpdate = suggestionRepository.findById(suggestion.getId()).orElse(null);
		SuggestionToUpdate.setCreated_at(suggestion.getCreated_at());
		SuggestionToUpdate.setAdopted_solution(suggestion.getAdopted_solution());
		SuggestionToUpdate.setChoices(suggestion.getChoices());
		SuggestionToUpdate.setReceipent_user(suggestion.getReceipent_user());
		SuggestionToUpdate.setFacilitator(suggestion.getFacilitator());
		SuggestionToUpdate.setType_id(suggestion.getType_id());
		SuggestionToUpdate.setUser_id(suggestion.getUser_id());
		SuggestionToUpdate.setProblem_amelioration(suggestion.getProblem_amelioration());
		SuggestionToUpdate.setProposed_solution(suggestion.getProposed_solution());
		SuggestionToUpdate.setJustification(suggestion.getJustification());
		SuggestionToUpdate.setComment(suggestion.getComment());
		SuggestionToUpdate.setAttachement_file(suggestion.getAttachement_file());
		SuggestionToUpdate.setUpdated_at(date);
		suggestionRepository.save(SuggestionToUpdate);
	}

	public void createGuestSuggestion(Suggestion suggestion) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
	    User Customuser= userRepository.findByEmail(userDetails.getUsername());
	    
		Date date = new Date();
		suggestion.setCreated_at(date);
		suggestion.setChoices("Pending");
		suggestion.setUpdated_at(null);
		suggestion.setJustification("");
		suggestion.setAdopted_solution("");
		suggestion.setComment("");
		//suggestion.setAttachement_file(null);
		suggestion.setUser_id(Customuser);
		suggestionRepository.save(suggestion);
		
	}
	
	
	
	/*public List<String> getSuggestionCountDepartment()
	{
	    List<String> department_count = suggestionRepository.DepartmentCount();
		List<String> department_count_array = new ArrayList<>();
		department_count_array.addAll(department_count);
		return department_count_array;
	}*/
	
	/*public List<String> getAllDpt()
	{
		  List<String> dpt_array = new ArrayList<>();
	      List<Department> users_dpt = departmentService.getAllDepartments();
	      $users_dptt = json_decode( $users_dpt );
	      $users_dp = json_decode(json_encode($users_dpt), true);
	      return $users_dp;
	}*/
	
}
