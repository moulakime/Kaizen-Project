package com.kaizen.project.controllers;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.kaizen.project.model.Department;
import com.kaizen.project.model.Suggestion;
import com.kaizen.project.model.Type;
import com.kaizen.project.model.User;
import com.kaizen.project.repository.SuggestionRepository;
import com.kaizen.project.repository.UserRepository;
import com.kaizen.project.services.DepartmentService;
import com.kaizen.project.services.EmailSenderService;
import com.kaizen.project.services.SuggestionService;
import com.kaizen.project.services.TypeService;

@Controller
public class SuggestionController {

	@Autowired
	private TypeService typeService;
	
	@Autowired
	private SuggestionRepository suggestionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SuggestionService suggestionService;
	
	@Autowired
    private EmailSenderService emailSenderService;
	
	@Autowired
	private DepartmentService departmentService;
	
	/* ----------------------------------- All Suggestions Portal ----------------------------------- */
	
	@RequestMapping(value = "/kaizen/portal/suggestionslist", method = RequestMethod.GET)
	public ModelAndView SuggestionList(Suggestion suggestion,Model model, ModelMap modelMap,HttpServletRequest request, Principal principal) {
		ModelAndView mav = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
	    User Customuser= userRepository.findByEmail(userDetails.getUsername());
	    Long id= Customuser.getId();
	    
	    int page = 0; //default page number is 0 (yes it is weird)
        int size = 5; //default page size is 5
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        
		request.setAttribute("suggestions", suggestionRepository.showAllSuggestions(PageRequest.of(page, size)));
		request.setAttribute("total_suggestion", suggestionRepository.count());
		request.setAttribute("total_suggestion_received", suggestionRepository.countSuggReceived(principal.getName()));
		request.setAttribute("total_suggestion_received_not_replied", suggestionRepository.countSuggReceivedNotReplied(principal.getName()));
		request.setAttribute("total_suggestion_sent_no_feedback", suggestionRepository.countSuggSentNoFeedBack(id));
		mav.addObject("committee_members" , userRepository.AllAdmin());
		mav.addObject("suggestion_sent" , suggestionRepository.countSuggSent(id));
		mav.addObject("logged_user" , userRepository.showConnectedUserData(principal.getName()));
		request.setAttribute("types", typeService.getAllTypes());
		mav.setViewName("/kaizen/portal/suggestionslist");
		return mav;
	}
	
	/* ----------------------------------- Suggestions Sent Portal ----------------------------------- */
	
	@RequestMapping(value = "/kaizen/portal/suggestionssent", method = RequestMethod.GET)
	public ModelAndView SuggestionSent(Suggestion suggestion,Model model, ModelMap modelMap,HttpServletRequest request, Principal principal) {
		
		ModelAndView mav = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
	    User Customuser= userRepository.findByEmail(userDetails.getUsername());
	    Long id= Customuser.getId();
	    
	    int page = 0; //default page number is 0 (yes it is weird)
        int size = 5; //default page size is 5
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        	    
	    mav.addObject("suggestion_sent", suggestionRepository.SuggestionSent(Customuser.getId(), PageRequest.of(page, size)));
		request.setAttribute("total_suggestion", suggestionRepository.count());
		request.setAttribute("total_suggestion_received", suggestionRepository.countSuggReceived(principal.getName()));
		request.setAttribute("total_suggestion_received_not_replied", suggestionRepository.countSuggReceivedNotReplied(principal.getName()));
		request.setAttribute("total_suggestion_sent_no_feedback", suggestionRepository.countSuggSentNoFeedBack(id));
		mav.addObject("committee_members" , userRepository.AllAdmin());
		mav.addObject("suggestion_sent_count" , suggestionRepository.countSuggSent(id));
		mav.addObject("logged_user" , userRepository.showConnectedUserData(principal.getName()));
		request.setAttribute("types", typeService.getAllTypes());
		mav.setViewName("/kaizen/portal/suggestionssent");
		return mav;
	}
	
	/* ----------------------------------- Suggestions Received Portal ----------------------------------- */
	
	@RequestMapping(value = "/kaizen/portal/suggestionsreceived", method = RequestMethod.GET)
	public ModelAndView SuggestionReceived(Suggestion suggestion,Model model, ModelMap modelMap,HttpServletRequest request, Principal principal) {
		
		ModelAndView mav = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
	    User Customuser= userRepository.findByEmail(userDetails.getUsername());
	    Long id= Customuser.getId();
	    
	    int page = 0; //default page number is 0 (yes it is weird)
        int size = 5; //default page size is 5
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        
		request.setAttribute("suggestion_received", suggestionRepository.SuggestionReceived(principal.getName(), PageRequest.of(page, size)));
		request.setAttribute("total_suggestion", suggestionRepository.count());
		request.setAttribute("total_suggestion_received", suggestionRepository.countSuggReceived(principal.getName()));
		request.setAttribute("total_suggestion_received_not_replied", suggestionRepository.countSuggReceivedNotReplied(principal.getName()));
		request.setAttribute("total_suggestion_sent_no_feedback", suggestionRepository.countSuggSentNoFeedBack(id));
		mav.addObject("committee_members" , userRepository.AllAdmin());
		mav.addObject("suggestion_sent" , suggestionRepository.countSuggSent(id));
		mav.addObject("logged_user" , userRepository.showConnectedUserData(principal.getName()));
		request.setAttribute("types", typeService.getAllTypes());
		mav.setViewName("/kaizen/portal/suggestionsreceived");
		return mav;
	}
	
/* ----------------------------------- All Suggestions Admin ----------------------------------- */
	
	@RequestMapping(value = "/kaizen/admin/suggestionslist", method = RequestMethod.GET)
	public ModelAndView AdminSuggestionList(Suggestion suggestion,Model model, ModelMap modelMap,HttpServletRequest request, Principal principal) {
		ModelAndView mav = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
	    User Customuser= userRepository.findByEmail(userDetails.getUsername());
	    Long id= Customuser.getId();
	    
	    int page = 0; //default page number is 0 (yes it is weird)
        int size = 5; //default page size is 5
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        
		request.setAttribute("suggestions", suggestionRepository.showAllSuggestions(PageRequest.of(page, size)));
		request.setAttribute("total_suggestion", suggestionRepository.count());
		request.setAttribute("total_suggestion_received", suggestionRepository.countSuggReceived(principal.getName()));
		request.setAttribute("total_suggestion_received_not_replied", suggestionRepository.countSuggReceivedNotReplied(principal.getName()));
		request.setAttribute("total_suggestion_sent_no_feedback", suggestionRepository.countSuggSentNoFeedBack(id));
	    mav.addObject("suggestion_sent" , suggestionRepository.countSuggSent(id));
		mav.addObject("logged_user" , userRepository.showConnectedUserData(principal.getName()));
		mav.addObject("committee_members" , userRepository.AllAdmin());
		request.setAttribute("types", typeService.getAllTypes());
		mav.setViewName("/kaizen/admin/suggestionslist");
		return mav;
	}
	
	/* ----------------------------------- Suggestions Sent Admin ----------------------------------- */
	
	@RequestMapping(value = "/kaizen/admin/suggestionssent", method = RequestMethod.GET)
	public ModelAndView AdminSuggestionSent(Suggestion suggestion,Model model, ModelMap modelMap,HttpServletRequest request, Principal principal) {
		
		ModelAndView mav = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
	    User Customuser= userRepository.findByEmail(userDetails.getUsername());
	    Long id= Customuser.getId();
	    
	    int page = 0; //default page number is 0 (yes it is weird)
        int size = 5; //default page size is 5
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        	    
	    mav.addObject("suggestion_sent", suggestionRepository.SuggestionSent(Customuser.getId(), PageRequest.of(page, size)));
		request.setAttribute("total_suggestion", suggestionRepository.count());
		request.setAttribute("total_suggestion_received", suggestionRepository.countSuggReceived(principal.getName()));
		request.setAttribute("total_suggestion_received_not_replied", suggestionRepository.countSuggReceivedNotReplied(principal.getName()));
		request.setAttribute("total_suggestion_sent_no_feedback", suggestionRepository.countSuggSentNoFeedBack(id));
	    mav.addObject("suggestion_sent_count" , suggestionRepository.countSuggSent(id));
		mav.addObject("logged_user" , userRepository.showConnectedUserData(principal.getName()));
		mav.addObject("committee_members" , userRepository.AllAdmin());
		request.setAttribute("types", typeService.getAllTypes());
		mav.setViewName("/kaizen/admin/suggestionssent");
		return mav;
	}
	
/* ----------------------------------- Suggestions Received Admin ----------------------------------- */
	
	@RequestMapping(value = "/kaizen/admin/suggestionsreceived", method = RequestMethod.GET)
	public ModelAndView AdminSuggestionReceived(Suggestion suggestion,Model model, ModelMap modelMap,HttpServletRequest request, Principal principal) {
		
		ModelAndView mav = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
	    User Customuser= userRepository.findByEmail(userDetails.getUsername());
	    Long id= Customuser.getId();
	    
	    int page = 0; //default page number is 0 (yes it is weird)
        int size = 5; //default page size is 5
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        
		request.setAttribute("suggestion_received", suggestionRepository.SuggestionReceived(principal.getName(), PageRequest.of(page, size)));
		request.setAttribute("total_suggestion", suggestionRepository.count());
		request.setAttribute("total_suggestion_received", suggestionRepository.countSuggReceived(principal.getName()));
		request.setAttribute("total_suggestion_received_not_replied", suggestionRepository.countSuggReceivedNotReplied(principal.getName()));
		request.setAttribute("total_suggestion_sent_no_feedback", suggestionRepository.countSuggSentNoFeedBack(id));
	    mav.addObject("suggestion_sent" , suggestionRepository.countSuggSent(id));
		mav.addObject("logged_user" , userRepository.showConnectedUserData(principal.getName()));
		mav.addObject("committee_members" , userRepository.AllAdmin());
		request.setAttribute("types", typeService.getAllTypes());
		mav.setViewName("/kaizen/admin/suggestionsreceived");
		return mav;
	}
	
/* ----------------------------------- Check if email exist ----------------------------------- */
	
	@RequestMapping(value = { "/email_not_allowed/{receipent}" }, method = RequestMethod.GET)
	@ResponseBody //added
	public void checkEmail(@PathVariable("receipent") String receipent, Suggestion suggestion , HttpServletRequest request,HttpServletResponse response,Principal principal) throws IOException {

	    response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();

	    try {

	        if (receipent.equals(principal.getName()) == false) {
	            out.println("<font color=#99cc99>\u2713 You can send suggestion to :  <b>"+receipent+"</b></font>");
	        }
	        else{
	        out.println("<font color=#ff6666>\u2715 You <b> can't </b> send a suggestion to yourself</font>");
	        }
	        out.println();



	    } catch (Exception ex) {

	        out.println("Error ->" + ex.getMessage());

	    } finally {
	        out.close();
	    }

	}
	
/* ----------------------------------- Check if email exist ----------------------------------- */
	
	@RequestMapping(value = { "/get_admins_email/{receipent}" }, method = RequestMethod.GET)
	@ResponseBody //added
	public void AdminListAutoComplete(@PathVariable("receipent") String receipent, Suggestion suggestion , HttpServletRequest request,HttpServletResponse response,Principal principal) throws IOException {

	    response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    List<User> users = userRepository.AdminsAutocomplete(receipent);
	    try {
	        if (userRepository.AdminsAutocomplete(receipent).isEmpty()) {

	        	out.println("<li class=\"dropdown-item\" style=\"text-align: center\"><a style=\"text-decoration:none\" class=\"h4 mb-0 d-none d-lg-inline-block\">No Email Found</a></li>");
	        }
	        else{
	        	for (User user : users) {
		            out.println("<li class='dropdown-item'><a id='get_email' value='${user.getEmail()}' onclick='getValue(this);'  style='text-decoration:none' class='h4 mb-0 d-none d-lg-inline-block'>" + user.getEmail() + "</a></li>");
		            
					}
	        }
	        out.println();



	    } catch (Exception ex) {

	        out.println("Error ->" + ex.getMessage());

	    } finally {
	        out.close();
	    }

	}
	
	/* ----------------------------------- Insert Suggestion ----------------------------------- */
	
	@SuppressWarnings("null")
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String AddSuggestion(@RequestParam("attachment_file") MultipartFile file ,@Valid Suggestion suggestion,BindingResult bindingResult , Model model , HttpServletRequest request, HttpServletResponse response, Principal principal) throws IOException {
		
		if(bindingResult.hasErrors())
		{
			request.setAttribute("bindingResult", bindingResult);
			//return "redirect:/kaizen/portal/suggestionslist?error";
		}
		if(suggestion.getReceipent_user().equals(principal.getName()))
		{
			return "redirect:/kaizen/portal/suggestionslist?NotAllowed";
		}
	    
		if(file.isEmpty() == false)
		{
			String folder = "src\\main\\resources\\static\\uploads\\attachements\\";
			byte[] bytes = file.getBytes();
			Path path = Paths.get(folder + file.getOriginalFilename());
			Files.write(path , bytes);
			suggestion.setAttachement_file(file.getOriginalFilename());
		}
		else if (file.isEmpty() == true)
		{
		suggestion.setAttachement_file(null);
		}
		suggestionService.createSuggestion(suggestion);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		String type = "";
		if(suggestion.getType_id().getId() == 1)
		{
			type = "SAFETY/ERGONOMICS";
		} else if (suggestion.getType_id().getId() == 2)
		{
			type = "EFFECTIVENESS";
		}
		else
		{
			type = "QUALITY";
		}
        mailMessage.setTo(suggestion.getReceipent_user());
        mailMessage.setSubject("New Suggestion Received");
        mailMessage.setFrom(suggestion.getUser_id().getEmail());
        mailMessage.setText("Hey there," + "\r\n"
        		+ " You have recently got a new Suggestion these are some informations about it :" + "\r\n"
        		+ " Creation Date : " + new SimpleDateFormat("dd MMMM yyyy").format(suggestion.getCreated_at()) + "\r\n"
        		+ " Sender Email : " + suggestion.getUser_id().getEmail() + "\r\n"
        		+ " Suggtion Type : " +  type + "\r\n"
        		+ " Problem Amelioration : " + suggestion.getProblem_amelioration() + "\r\n"
        		+ " Proposed Solution : " + suggestion.getProposed_solution());
        emailSenderService.sendEmail(mailMessage);
        mailMessage.setTo(suggestion.getUser_id().getEmail());
        mailMessage.setSubject("New Suggestion Sent");
        mailMessage.setFrom("no-reply@kaizen.com");
        mailMessage.setText("Hey there," + "\r\n"
        		+ " You have recently sent a new Suggestion these are some informations about it :" + "\r\n"
        		+ " Creation Date : " + new SimpleDateFormat("dd MMMM yyyy").format(suggestion.getCreated_at()) + "\r\n"
        		+ " Receipent User Email : " + suggestion.getReceipent_user() + "\r\n"
        		+ " Suggtion Type : " +  type + "\r\n"
        		+ " Problem Amelioration : " + suggestion.getProblem_amelioration() + "\r\n"
        		+ " Proposed Solution : " + suggestion.getProposed_solution());
        emailSenderService.sendEmail(mailMessage);
		return "redirect:/kaizen/portal/suggestionslist?success";
	}
	
/* ----------------------------------- Insert Suggestion ----------------------------------- */
	
	@SuppressWarnings("null")
	@RequestMapping(value = "/insert_guest", method = RequestMethod.POST)
	public String AddGuestSuggestion(@RequestParam("attachment_file") MultipartFile file ,@Valid Suggestion suggestion,BindingResult bindingResult , Model model , HttpServletRequest request, HttpServletResponse response, Principal principal) throws IOException {
 
		if(bindingResult.hasErrors())
		{
			request.setAttribute("bindingResult", bindingResult);
			//return "redirect:/kaizen/admin/suggestionslist?error";
		}
		if(suggestion.getReceipent_user().equals(principal.getName()))
		{
			return "redirect:/kaizen/admin/suggestionslist?NotAllowed";
		}
	    
		if(file.isEmpty() == false)
		{
			String folder = "src\\main\\resources\\static\\uploads\\attachements\\";
			byte[] bytes = file.getBytes();
			Path path = Paths.get(folder + file.getOriginalFilename());
			Files.write(path , bytes);
			suggestion.setAttachement_file(file.getOriginalFilename());
		}
		else if (file.isEmpty() == true)
		{
		suggestion.setAttachement_file(null);
		}
		suggestionService.createGuestSuggestion(suggestion);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(suggestion.getReceipent_user());
        mailMessage.setSubject("New Suggestion Received");
        mailMessage.setFrom(suggestion.getUser_id().getEmail());
        mailMessage.setText("Hey there," + "\r\n"
        		+ " You have recently got a new Suggestion these are some informations about it :" + "\r\n"
        		+ " Creation Date : " + new SimpleDateFormat("dd MMMM yyyy").format(suggestion.getCreated_at()) + "\r\n"
        		+ " Sender Email : " + suggestion.getUser_id().getEmail() + "\r\n"
        		+ " Suggtion Type : " + suggestion.getType_id().getId() + "\r\n"
        		+ " Problem Amelioration : " + suggestion.getProblem_amelioration() + "\r\n"
        		+ " Proposed Solution : " + suggestion.getProposed_solution());
        emailSenderService.sendEmail(mailMessage);
		return "redirect:/kaizen/admin/suggestionslist?success";
	}
	
	
	/* ----------------------------------- Update Suggestion Portal ----------------------------------- */
	
	@RequestMapping(value = "/update_sugg", method = RequestMethod.POST)
	public String UpdateSuggestion(@ModelAttribute("suggestion") Suggestion suggestion,BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
		{
			return "redirect:/kaizen/portal/suggestionsreceived?Error";
			//return "Errors : " + bindingResult.getAllErrors();
		}
		Suggestion suggestionToUpdate = suggestionRepository.findById(suggestion.getId()).orElse(null);
		if(suggestionToUpdate == null)
		{
			return "redirect:/kaizen/portal/suggestionsreceived?NotFound";
		}
		else
		{
		suggestionService.updateSuggestion(suggestion);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		String type = "";
		if(suggestion.getType_id().getId() == 1)
		{
			type = "SAFETY/ERGONOMICS";
		} else if (suggestion.getType_id().getId() == 2)
		{
			type = "EFFECTIVENESS";
		}
		else
		{
			type = "QUALITY";
		}
        mailMessage.setTo(suggestion.getReceipent_user());
        mailMessage.setSubject("You update a Suggestion");
        mailMessage.setFrom("no-reply@kaizen.com");
        mailMessage.setText("Hey there," + "\r\n"
        		+ " You have recently update a Suggestion these are some informations about it :" + "\r\n"
        		+ " Creation Date : " + new SimpleDateFormat("dd MMMM yyyy").format(suggestionToUpdate.getCreated_at()) + "\r\n"
        		+ " Sender Email : " + suggestionToUpdate.getUser_id().getEmail() + "\r\n"
        		+ " Suggtion Type : " + type + "\r\n"
        		+ " Problem Amelioration : " + suggestionToUpdate.getProblem_amelioration() + "\r\n"
        		+ " Proposed Solution : " + suggestionToUpdate.getProposed_solution()+ " Adopted Solution : " + suggestionToUpdate.getAdopted_solution()
        		+ " Status : " + suggestionToUpdate.getChoices());
        emailSenderService.sendEmail(mailMessage);
        mailMessage.setTo(suggestion.getUser_id().getEmail());
        mailMessage.setSubject("Updated Suggestion");
        mailMessage.setFrom("no-reply@kaizen.com");
        mailMessage.setText("Hey there," + "\r\n"
        		+ " Your Suggeestion recently updated, these are some informations about it :" + "\r\n"
        		+ " Creation Date : " + new SimpleDateFormat("dd MMMM yyyy").format(suggestionToUpdate.getCreated_at()) + "\r\n"
        		+ " Updated by : " + suggestionToUpdate.getReceipent_user() + "\r\n"
        		+ " Suggtion Type : " + type + "\r\n"
        		+ " Problem Amelioration : " + suggestionToUpdate.getProblem_amelioration() + "\r\n"
        		+ " Proposed Solution : " + suggestionToUpdate.getProposed_solution()+ " Adopted Solution : " + suggestionToUpdate.getAdopted_solution()
        		+ " Status : " + suggestionToUpdate.getChoices());
        emailSenderService.sendEmail(mailMessage);
		return "redirect:/kaizen/portal/suggestionsreceived?Success";
		}
	}
	
/* ----------------------------------- Update Suggestion Admin ----------------------------------- */
	
	@RequestMapping(value = "/update_sugg_a", method = RequestMethod.POST)
	public String UpdateSuggestionA(@ModelAttribute("suggestion") Suggestion suggestion,BindingResult bindingResult) {
		
		if(bindingResult.hasErrors())
		{
			return "redirect:/kaizen/portal/suggestionsreceived?Error";
			//return "Errors : " + bindingResult.getAllErrors();
		}
		Suggestion suggestionToUpdate = suggestionRepository.findById(suggestion.getId()).orElse(null);
		if(suggestionToUpdate == null)
		{
			return "redirect:/kaizen/portal/suggestionsreceived?NotFound";
		}
		else
		{
			if(suggestionToUpdate.getAttachement_file() == null)
			{
				suggestion.setAttachement_file(null);
			}
		suggestionService.updateSuggestion(suggestion);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		String type = "";
		if(suggestion.getType_id().getId() == 1)
		{
			type = "SAFETY/ERGONOMICS";
		} else if (suggestion.getType_id().getId() == 2)
		{
			type = "EFFECTIVENESS";
		}
		else
		{
			type = "QUALITY";
		}
		 mailMessage.setTo(suggestion.getReceipent_user());
	        mailMessage.setSubject("You update a Suggestion");
	        mailMessage.setFrom("no-reply@kaizen.com");
	        mailMessage.setText("Hey there," + "\r\n"
	        		+ " You have recently update a Suggestion these are some informations about it :" + "\r\n"
	        		+ " Creation Date : " + new SimpleDateFormat("dd MMMM yyyy").format(suggestionToUpdate.getCreated_at()) + "\r\n"
	        		+ " Sender Email : " + suggestionToUpdate.getUser_id().getEmail() + "\r\n"
	        		+ " Suggtion Type : " + type + "\r\n"
	        		+ " Problem Amelioration : " + suggestionToUpdate.getProblem_amelioration() + "\r\n"
	        		+ " Proposed Solution : " + suggestionToUpdate.getProposed_solution()
	        		+ " Adopted Solution : " + suggestionToUpdate.getAdopted_solution()
	        		+ " Status : " + suggestionToUpdate.getChoices());
	        emailSenderService.sendEmail(mailMessage);
	        mailMessage.setTo(suggestion.getUser_id().getEmail());
	        mailMessage.setSubject("Updated Suggestion");
	        mailMessage.setFrom("no-reply@kaizen.com");
	        mailMessage.setText("Hey there," + "\r\n"
	        		+ " Your Suggeestion recently updated, these are some informations about it :" + "\r\n"
	        		+ " Creation Date : " + new SimpleDateFormat("dd MMMM yyyy").format(suggestionToUpdate.getCreated_at()) + "\r\n"
	        		+ " Updated by : " + suggestionToUpdate.getReceipent_user() + "\r\n"
	        		+ " Suggtion Type : " + type + "\r\n"
	        		+ " Problem Amelioration : " + suggestionToUpdate.getProblem_amelioration() + "\r\n"
	        		+ " Proposed Solution : " + suggestionToUpdate.getProposed_solution()
	        		+ " Adopted Solution : " + suggestionToUpdate.getAdopted_solution()
	        		+ " Status : " + suggestionToUpdate.getChoices());
        emailSenderService.sendEmail(mailMessage);
		return "redirect:/kaizen/admin/suggestionsreceived?Success";
		}
	}
	
/* ----------------------------------- All Suggestions Admin ----------------------------------- */
	
	@RequestMapping(value = "/kaizen/admin/report", method = RequestMethod.GET)
	public ModelAndView Report(Suggestion suggestion,Model model, ModelMap modelMap,HttpServletRequest request, Principal principal) {
		ModelAndView mav = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
	    User Customuser= userRepository.findByEmail(userDetails.getUsername());
	    Long id= Customuser.getId();
	    
	    int page = 0; //default page number is 0 (yes it is weird)
        int size = 5; //default page size is 5
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        
		request.setAttribute("suggestions", suggestionRepository.showAllSuggestions(PageRequest.of(page, size)));
		request.setAttribute("total_suggestion", suggestionRepository.count());
		request.setAttribute("total_suggestion_received", suggestionRepository.countSuggReceived(principal.getName()));
		request.setAttribute("total_suggestion_received_not_replied", suggestionRepository.countSuggReceivedNotReplied(principal.getName()));
		request.setAttribute("total_suggestion_sent_no_feedback", suggestionRepository.countSuggSentNoFeedBack(id));
	    mav.addObject("suggestion_sent" , suggestionRepository.countSuggSent(id));
		mav.addObject("user_details" , userRepository.showConnectedUserData(principal.getName()));
		mav.addObject("committee_members" , userRepository.AllAdmin());
		mav.addObject("departments", departmentService.getAllDepartments());
		request.setAttribute("types", typeService.getAllTypes());
		mav.setViewName("/kaizen/admin/report");
		return mav;
	}
	
/* ----------------------------------- Insert Suggestion Admin ----------------------------------- */
	
	@SuppressWarnings("null")
	@RequestMapping(value = "/insertA", method = RequestMethod.POST)
	public String AddASuggestion(@RequestParam("attachment_file") MultipartFile file ,@Valid Suggestion suggestion,BindingResult bindingResult , Model model , HttpServletRequest request, HttpServletResponse response, Principal principal) throws IOException {
		
		if(bindingResult.hasErrors())
		{
			request.setAttribute("bindingResult", bindingResult);
			//return "redirect:/kaizen/admin/suggestionslist?error";
		}
		if(suggestion.getReceipent_user().equals(principal.getName()))
		{
			return "redirect:/kaizen/admin/suggestionslist?NotAllowed";
		}
	    
		if(file.isEmpty() == false)
		{
		String folder = "src\\main\\resources\\static\\uploads\\attachements\\";
		byte[] bytes = file.getBytes();
		Path path = Paths.get(folder + file.getOriginalFilename());
		Files.write(path , bytes);
		suggestion.setAttachement_file(file.getOriginalFilename());
		}
		else if (file.isEmpty() == true)
		{
		suggestion.setAttachement_file(null);
		}
		suggestionService.createSuggestion(suggestion);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		String type = "";
		if(suggestion.getType_id().getId() == 1)
		{
			type = "SAFETY/ERGONOMICS";
		} else if (suggestion.getType_id().getId() == 2)
		{
			type = "EFFECTIVENESS";
		}
		else
		{
			type = "QUALITY";
		}
        mailMessage.setTo(suggestion.getReceipent_user());
        mailMessage.setSubject("New Suggestion Received");
        mailMessage.setFrom(suggestion.getUser_id().getEmail());
        mailMessage.setText("Hey there," + "\r\n"
        		+ " You have recently got a new Suggestion these are some informations about it :" + "\r\n"
        		+ " Creation Date : " + new SimpleDateFormat("dd MMMM yyyy").format(suggestion.getCreated_at()) + "\r\n"
        		+ " Sender Email : " + suggestion.getUser_id().getEmail() + "\r\n"
        		+ " Suggtion Type : " +  type + "\r\n"
        		+ " Problem Amelioration : " + suggestion.getProblem_amelioration() + "\r\n"
        		+ " Proposed Solution : " + suggestion.getProposed_solution());
        emailSenderService.sendEmail(mailMessage);
        mailMessage.setTo(suggestion.getUser_id().getEmail());
        mailMessage.setSubject("New Suggestion Sent");
        mailMessage.setFrom("no-reply@kaizen.com");
        mailMessage.setText("Hey there," + "\r\n"
        		+ " You have recently sent a new Suggestion these are some informations about it :" + "\r\n"
        		+ " Creation Date : " + new SimpleDateFormat("dd MMMM yyyy").format(suggestion.getCreated_at()) + "\r\n"
        		+ " Receipent User Email : " + suggestion.getReceipent_user() + "\r\n"
        		+ " Suggtion Type : " +  type + "\r\n"
        		+ " Problem Amelioration : " + suggestion.getProblem_amelioration() + "\r\n"
        		+ " Proposed Solution : " + suggestion.getProposed_solution());
        emailSenderService.sendEmail(mailMessage);
		return "redirect:/kaizen/admin/suggestionslist?success";
	}
/* ----------------------------------- Check if email exist ----------------------------------- */

/* ----------------------------------- Search by department ----------------------------------- */
	@RequestMapping(value = { "/reporting/{department}" }, method = RequestMethod.GET)
	@ResponseBody //added
	public void ReportingD(@PathVariable("department") String department,Suggestion suggestion,HttpServletRequest request,HttpServletResponse response,Principal principal) throws IOException {

	    response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    List<Suggestion> suggestions = suggestionRepository.ReportingBySuggestion(department);
	    	 try {
	    		 if(suggestions.isEmpty())
	 	    	{
	 	    		out.println("<tr><td colspan='7' style='text-align: center;'><b>No Data Found</b></td></tr>");
	 	    	}
	 	    	else
	 	    	{
	 	    		for (Suggestion sugg : suggestions) {
	 		 	    	String strDateFormat = "dd/MMM/yyyy";
	 		 	   		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
	 		 	   		String formattedDate= dateFormat.format(sugg.getCreated_at());
	 	    		out.println("<tr><td><b> " + formattedDate + "</b></td>"
	 	    			+ "<td>" + sugg.getUser_id().getFirst_name() + " " + sugg.getUser_id().getLast_name() + "</td>"
	 	    			+ "<td>" + sugg.getType_id().getSugg_type() + "</td>"
	 	    			+ "<td>" + sugg.getProblem_amelioration() + "</td>"
	 	    			+ "<td>" + sugg.getProposed_solution() + "</td>"
	 	    			+ "<td><b>" + sugg.getChoices() + "</b></td>"
	 	    			+ "<td>" + sugg.getAdopted_solution() + "</td></tr>");
	 	    	}
	 	    	}
	 	        out.println();



	 	    } catch (Exception ex) {

	 	        out.println("Error ->" + ex.getMessage());

	 	    } finally {
	 	        out.close();
	 	    }

	}
	
	/* ----------------------------------- Search by department ----------------------------------- */
	
	/* ----------------------------------- Search by status ----------------------------------- */
	@RequestMapping(value = { "/reporting/{suggestion_status}" }, method = RequestMethod.GET)
	@ResponseBody //added
	public void ReportingS(@PathVariable("suggestion_status") String status,Suggestion suggestion,HttpServletRequest request,HttpServletResponse response,Principal principal) throws IOException {

	    response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    List<Suggestion> suggestions = suggestionRepository.ReportingBySuggestionStatus(status);
	    	 try {
	    		 if(suggestions.isEmpty())
	 	    	{
	 	    		out.println("<tr><td colspan='7' style='text-align: center;'><b>No Data Found</b></td></tr>");
	 	    	}
	 	    	else
	 	    	{
	 	    		for (Suggestion sugg : suggestions) {
	 		 	    	String strDateFormat = "dd/MMM/yyyy";
	 		 	   		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
	 		 	   		String formattedDate= dateFormat.format(sugg.getCreated_at());
	 	    		out.println("<tr><td><b> " + formattedDate + "</b></td>"
	 	    			+ "<td>" + sugg.getUser_id().getFirst_name() + " " + sugg.getUser_id().getLast_name() + "</td>"
	 	    			+ "<td>" + sugg.getType_id().getSugg_type() + "</td>"
	 	    			+ "<td>" + sugg.getProblem_amelioration() + "</td>"
	 	    			+ "<td>" + sugg.getProposed_solution() + "</td>"
	 	    			+ "<td><b>" + sugg.getChoices() + "</b></td>"
	 	    			+ "<td>" + sugg.getAdopted_solution() + "</td></tr>");
	 	    	}
	 	    	}
	 	        out.println();



	 	    } catch (Exception ex) {

	 	        out.println("Error ->" + ex.getMessage());

	 	    } finally {
	 	        out.close();
	 	    }

	}
	
	/* ----------------------------------- Search by status ----------------------------------- */
	
	/* ----------------------------------- Search by department and status ----------------------------------- */
	@RequestMapping(value = { "/reporting/{department}/{suggestion_status}/{suggestion_type}/{date_begin}/{date_end}" }, method = RequestMethod.GET)
	@ResponseBody //added
	public void ReportingDS(@PathVariable("department") String department,@PathVariable("suggestion_status") String status,@PathVariable("suggestion_type") Long type,@PathVariable("date_begin") String date_begin,@PathVariable("date_end") String date_end,Suggestion suggestion,HttpServletRequest request,HttpServletResponse response,Principal principal) throws IOException {

	    response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    List<Suggestion> suggestions = suggestionRepository.ReportingBySuggestionDepartmentStatus(department, status, type, date_begin, date_end);
	    	 try {
	    		 if(suggestions.isEmpty())
	 	    	{
	 	    		out.println("<tr><td colspan='7' style='text-align: center;'><b>No Data Found</b></td></tr>");
	 	    	}
	 	    	else
	 	    	{
	 	    		for (Suggestion sugg : suggestions) {
	 		 	    	String strDateFormat = "dd/MMM/yyyy";
	 		 	   		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
	 		 	   		String formattedDate= dateFormat.format(sugg.getCreated_at());
	 	    		out.println("<tr><td><b> " + formattedDate + "</b></td>"
	 	    			+ "<td>" + sugg.getUser_id().getFirst_name() + " " + sugg.getUser_id().getLast_name() + "</td>"
	 	    			+ "<td>" + sugg.getType_id().getSugg_type() + "</td>"
	 	    			+ "<td>" + sugg.getProblem_amelioration() + "</td>"
	 	    			+ "<td>" + sugg.getProposed_solution() + "</td>"
	 	    			+ "<td><b>" + sugg.getChoices() + "</b></td>"
	 	    			+ "<td>" + sugg.getAdopted_solution() + "</td></tr>");
	 	    	}
	 	    	}
	 	        out.println();



	 	    } catch (Exception ex) {

	 	        out.println("Error ->" + ex.getMessage());

	 	    } finally {
	 	        out.close();
	 	    }

	}
	
	/* ----------------------------------- Search by department and status ----------------------------------- */

	
	@RequestMapping(value = { "/chart-data-department" }, method = RequestMethod.GET)
	@ResponseBody //added
	public void ChartDataDepartment(Suggestion suggestion,HttpServletRequest request,HttpServletResponse response,Principal principal) throws IOException {

		List<String> suggestions = suggestionRepository.ChartPerDepartment();
		Gson gson = new Gson();
		String jsonString = gson.toJson(suggestions);
		response.setContentType("application/json");
		response.getWriter().write(jsonString);
		System.out.println(jsonString);
	}
	
	@RequestMapping(value = { "/chart-data-type" }, method = RequestMethod.GET)
	@ResponseBody //added
	public void ChartDataType(Suggestion suggestion,HttpServletRequest request,HttpServletResponse response,Principal principal) throws IOException {

		List<String> types = suggestionRepository.ChartPerType();
		Gson gson = new Gson();
		String jsonString = gson.toJson(types);
		response.setContentType("application/json");
		response.getWriter().write(jsonString);
	}
	
	@RequestMapping(value = { "/chart-data" }, method = RequestMethod.GET)
	@ResponseBody //added
	public void ChartDataMonthly(Suggestion suggestion,HttpServletRequest request,HttpServletResponse response,Principal principal) throws IOException {

		List<String> monthlyData = suggestionRepository.ChartPerMonth();
		Gson gson = new Gson();
		String jsonString = gson.toJson(monthlyData);
		response.setContentType("application/json");
		response.getWriter().write(jsonString);
	}
	
	
	@RequestMapping(value = "/kaizen/admin/getFile", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public byte[] getFile(String filename) throws IOException {
	    File file = new File("src\\main\\resources\\static\\uploads\\attachements\\"+filename);
		return IOUtils.toByteArray(new FileInputStream(file));
	}
	
	
	
}
