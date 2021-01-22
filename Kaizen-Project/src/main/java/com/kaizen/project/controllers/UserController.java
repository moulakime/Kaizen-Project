package com.kaizen.project.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kaizen.project.model.Suggestion;
import com.kaizen.project.model.User;
import com.kaizen.project.repository.SuggestionRepository;
import com.kaizen.project.repository.UserRepository;
import com.kaizen.project.services.DepartmentService;
import com.kaizen.project.services.EmailSenderService;
import com.kaizen.project.services.TypeService;
import com.kaizen.project.services.UserService;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private EmailSenderService emailSenderService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private SuggestionRepository suggestionRepository;
	
	@Autowired
	private TypeService typeService;
	
	/* ----------------------------------- Check if immatricule exist ----------------------------------- */
	
	@RequestMapping(value = { "/exist/{immatricule}" }, method = RequestMethod.GET)
	public void checkImmatricule(@PathVariable("immatricule") String immatricule, User user ,HttpServletResponse response) throws IOException {

	    response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();

	    try {

	        if (userRepository.findByImmatricule(user.getImmatricule()) == null) {
	            out.println("<font color=#99cc99>\u2713 You can use <b>"+immatricule+"</b></font>");
	        }
	        else{
	        out.println("<font color=#ff6666>\u2715<b> "+immatricule+"</b> is already in use</font>");
	        }
	        out.println();



	    } catch (Exception ex) {

	        out.println("Error ->" + ex.getMessage());

	    } finally {
	        out.close();
	    }

	}
	
	/* ----------------------------------- Check if email exist ----------------------------------- */
	
	@RequestMapping(value = { "/exist_email/{email}" }, method = RequestMethod.GET)
	public void checkEmail(@PathVariable("email") String email, User user ,HttpServletResponse response) throws IOException {

	    response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();

	    try {

	        if (userRepository.findByEmail(user.getEmail()) == null) {
	            out.println("<font color=#99cc99>\u2713 You can use <b>"+email+"</b></font>");
	        }
	        else{
	        out.println("<font color=#ff6666>\u2715<b> "+email+"</b> is already in use</font>");
	        }
	        out.println();



	    } catch (Exception ex) {

	        out.println("Error ->" + ex.getMessage());

	    } finally {
	        out.close();
	    }

	}

	/* ----------------------------------- Register the new user ----------------------------------- */
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String processRegister(@Valid User user, BindingResult bindingResult , Model model , HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView();
		
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

		
		// Check for the validation
		
		if(bindingResult.hasErrors())
		{
			model.addAttribute("bindingResult", bindingResult);
			return ("kaizen/auth/register");
		} 
		if(userRepository.findByEmail(user.getEmail()) != null)
		{
			model.addAttribute("Message_email", "Email already exist!!");
			mav.addObject("departments", departmentService.getAllDepartments());
			return ("kaizen/auth/register");
		}
		if(userRepository.findByImmatricule(user.getImmatricule()) != null)
		{
			model.addAttribute("Message", "Immatricule already exist!!");
			mav.addObject("departments", departmentService.getAllDepartments());
			return ("kaizen/auth/register");
		}
		else
		{
			
			userService.createUser(user);
			/* Test sending email */
			SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("noreply-kaizen@lear.com");
            mailMessage.setText("Hey " + user.getFirst_name() + ", ");
            mailMessage.setText("Hey " + user.getFirst_name() + "," + "\r\n"
            		+ " You recently registered for Kaizen Plateform. Please log in with your credentials." + "\r\n"
            		+ " Thanks." + "\r\n"
            		+ "Team Kaizen");
            emailSenderService.sendEmail(mailMessage);
            mav.addObject("email", user.getEmail());
			/* Test sending email */    
              
		}
		model.addAttribute("successMessage", "You can now login with your credentials");
		//return new ModelAndView("login");
		return "redirect:/register?success";
	}
	
	/* ----------------------------------- Show Dashboard ----------------------------------- */
	
	@RequestMapping(value = "/kaizen/admin/dashboard", method = RequestMethod.GET)
	public ModelAndView Dashboard(User user,Model model, ModelMap modelMap, Principal principal) {
		/*User auth_user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String first_name = auth_user.getFirst_name();*/
		ModelAndView mav = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
	    User Customuser= userRepository.findByEmail(userDetails.getUsername());
	    Long id= Customuser.getId();
		
		model.addAttribute("suggestion", new Suggestion());
		mav.addObject("total_suggestion", suggestionRepository.count());
		//mav.addObject("total_suggestion_per_type", suggestionRepository.CountSuggestionByType());
		mav.addObject("types", typeService.getAllTypes());
		mav.addObject("total_suggestion_received", suggestionRepository.countSuggReceived(principal.getName()));
		mav.addObject("total_suggestion_received_not_replied", suggestionRepository.countSuggReceivedNotReplied(principal.getName()));
		mav.addObject("total_suggestion_sent_no_feedback", suggestionRepository.countSuggSentNoFeedBack(id));
		mav.addObject("suggestion_count", suggestionRepository.countAllSugg());
		mav.addObject("suggestion_count_completed", suggestionRepository.countTotalCompleted());
		mav.addObject("suggestion_count_waiting", suggestionRepository.countTotalWaiting());
		mav.addObject("user_details" , userRepository.showConnectedUserData(principal.getName()));
		mav.addObject("committee_members" , userRepository.AllAdmin());
		mav.setViewName("/kaizen/admin/Dashboard");
		return mav;
	}
	
	/* ----------------------------------- Show profile by role ----------------------------------- */
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView UserProfile(User user,Model model, ModelMap modelMap, Principal principal) {
		ModelAndView mav = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		List<String> roles = new ArrayList<String>();
		for (GrantedAuthority a: authorities)
		{
			roles.add(a.getAuthority());
		}
		
		if(roles.contains("ROLE_ADMIN"))
		{
			mav.setViewName("profile");
		}
		else if(roles.contains("ROLE_USER"))
		{
			mav.setViewName("user_profile");
		}
		/*UserDetails userDetails = (UserDetails) auth.getPrincipal();
		mav.addObject("team_member", userRepository.TeamMember(userDetails.getUsername()));*/
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
	    User Customuser= userRepository.findByEmail(userDetails.getUsername());
	    Long id= Customuser.getId();
	    String dpt= Customuser.getDepartment();
	    mav.addObject("suggestion_sent" , suggestionRepository.countSuggSent(id));
	    mav.addObject("team_member" , userRepository.TeamMember(dpt));
	    mav.addObject("team_member_count" , userRepository.TeamMemberCount(dpt));
	    mav.addObject("committee_members" , userRepository.AllAdmin());
		mav.addObject("user_details" , userRepository.showConnectedUserData(principal.getName()));
		return mav;
	}
	
	/* ----------------------------------- All users ----------------------------------- */
	
	@RequestMapping(value = "/kaizen/admin/users", method = RequestMethod.GET)
	public ModelAndView UsersList(User user,Model model, ModelMap modelMap, HttpServletRequest request, Principal principal) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/kaizen/admin/users");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
	    User Customuser= userRepository.findByEmail(userDetails.getUsername());
	    Long id= Customuser.getId();
	    Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		List<String> roles = new ArrayList<String>();
		for (GrantedAuthority a: authorities)
		{
			roles.add(a.getAuthority());
		}
		
		int page = 0; //default page number is 0 (yes it is weird)
	    int size = 5; //default page size is 5
	        
	    if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
	          page = Integer.parseInt(request.getParameter("page")) - 1;
	    }

	    if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
	          size = Integer.parseInt(request.getParameter("size"));
	    }
		
		request.setAttribute("roles", roles);
		mav.addObject("total_user_count" , userRepository.TotalUserCount());
		mav.addObject("total_admin_count" , userRepository.TotalAdminCount());
		mav.addObject("committee_members" , userRepository.AllAdmin());
		request.setAttribute("total_suggestion", suggestionRepository.count());
		request.setAttribute("departments", departmentService.getAllDepartments());
		request.setAttribute("total_suggestion_received", suggestionRepository.countSuggReceived(principal.getName()));
		request.setAttribute("total_suggestion_received_not_replied", suggestionRepository.countSuggReceivedNotReplied(principal.getName()));
		request.setAttribute("total_suggestion_sent_no_feedback", suggestionRepository.countSuggSentNoFeedBack(id));
		mav.addObject("logged_user" , userRepository.showConnectedUserData(principal.getName()));
		mav.addObject("users_details" , userRepository.showAllUsersData(PageRequest.of(page, size)));
		return mav;
	}
	
	/* ----------------------------------- Update user profile ----------------------------------- */
	
	  @RequestMapping(value = "/update", method = RequestMethod.POST) 
	  public String UserProfileUpdate(@ModelAttribute("user") User user,BindingResult bindingResult) { 
	  
		  ModelAndView mav = new ModelAndView();
		  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		  Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		  List<String> roles = new ArrayList<String>();
		  User userToUpdate = userRepository.findById(user.getId()).orElse(null);
		  if(bindingResult.hasErrors())
			{
				//return "redirect:/kaizen/portal/suggestionsreceived?Error";
				mav.setViewName("Error");
			}
		  for(GrantedAuthority a: authorities)
		  {
			  roles.add(a.getAuthority());
		  }
		  if(roles.contains("ROLE_ADMIN"))
		  {
			  mav.setViewName("profile");
		  }
		  else if(roles.contains("ROLE_USER"))
		  {
			  mav.setViewName("user_profile");
		  }
		  userService.updateUser(userToUpdate);
		  //return "redirect:/profile?Success";
		 // return mav;
		  return "redirect:/profile?Success";
	  }
	  
	  /* ----------------------------------- Delete user from table users ----------------------------------- */
	  @RequestMapping(value = "/delete", method = RequestMethod.POST) 
	  //@DeleteMapping("/delete")
	  public String DeleteUser(@ModelAttribute("user") User user,BindingResult bindingResult) { 
	  
		  User userToDelete = userRepository.findById(user.getId()).orElse(null);	
		  if(bindingResult.hasErrors())
			{
				return "redirect:/kaizen/admin/users?Error";
			}
		  if(userToDelete == null)
		  {
			  return "redirect:/kaizen/admin/users?NotFound";
		  }
		  userService.deleteUser(userToDelete);
		  return "redirect:/kaizen/admin/users?Success";  
	  
	  }
	  
	  /* ----------------------------------- Update users role ----------------------------------- */
		
	  @RequestMapping(value = "/upgrade", method = RequestMethod.POST) 
	  public String UserRoleUpdate(@RequestParam("user") String email,@ModelAttribute("user") User user,BindingResult bindingResult) { 
	  
		  ModelAndView mav = new ModelAndView();
		  User userToUpdate = userRepository.findByEmail(user.getEmail());
		  if(bindingResult.hasErrors())
			{
				return "redirect:/kaizen/portal/suggestionsreceived?Error";
			}
		  
		  userService.upgradeUser(userToUpdate);
		  return "redirect:/kaizen/admin/users?Success";
		  //return mav;
	  }
	  
		@RequestMapping(value = "/update_profile", method = RequestMethod.POST)
		public String UpdateUser(@RequestParam("avatar") MultipartFile avatar ,@RequestParam("password") String password,@Valid User user,BindingResult bindingResult , Model model , HttpServletRequest request, HttpServletResponse response) throws IOException {
			User userToUpdate = userRepository.getOne(user.getId());
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if(bindingResult.hasErrors())
			{
			request.setAttribute("bindingResult", bindingResult);	
			}
		    
			if(avatar.isEmpty() == false){
			Date date = new Date();
			String folder = "src\\main\\resources\\static\\uploads\\avatars\\";
			byte[] bytes = avatar.getBytes();
			Path path = Paths.get(folder + user.getId());
			Files.write(path , bytes);
				/*Date date = new Date();
				String strDateFormat = "yyyymmddhhmmss";
				DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
				String formattedDate= dateFormat.format(date);
				String folder = "src\\main\\resources\\static\\uploads\\avatars\\";
				byte[] bytes = avatar.getBytes();
				Path path = Paths.get(folder + (formattedDate + avatar.getOriginalFilename()));
				Files.write(path , bytes);*/
			userToUpdate.setFirst_name(user.getFirst_name());
			userToUpdate.setLast_name(user.getLast_name());
			userToUpdate.setDepartment(user.getDepartment());
			userToUpdate.setJob_title(user.getJob_title());
			userToUpdate.setSupervisor(user.getSupervisor());
			userToUpdate.setTown(user.getTown());
			userToUpdate.setCountry(user.getCountry());
			userToUpdate.setPassword(encoder.encode(user.getPassword()));
			userToUpdate.setAvatar(avatar.getOriginalFilename());
			userToUpdate.setUpdated_at(date);
			userRepository.save(userToUpdate);
			}
			else if (avatar.isEmpty() == true)
			{
				Date date = new Date();
				/*String folder = "src\\main\\resources\\static\\uploads\\avatars\\";
				byte[] bytes = avatar.getBytes();
				Path path = Paths.get(folder + user.getId());
				Files.write(path , bytes);*/
				userToUpdate.setFirst_name(user.getFirst_name());
				userToUpdate.setLast_name(user.getLast_name());
				userToUpdate.setDepartment(user.getDepartment());
				userToUpdate.setJob_title(user.getJob_title());
				userToUpdate.setSupervisor(user.getSupervisor());
				userToUpdate.setTown(user.getTown());
				userToUpdate.setCountry(user.getCountry());
				userToUpdate.setPassword(encoder.encode(user.getPassword()));
				userToUpdate.setAvatar(user.getAvatar());
				userToUpdate.setUpdated_at(date);
				userRepository.save(userToUpdate);
			}
			/*if(password == null)
			{
				Date date = new Date();
				/*String folder = "src\\main\\resources\\static\\uploads\\avatars\\";
				byte[] bytes = avatar.getBytes();
				Path path = Paths.get(folder + user.getId());
				Files.write(path , bytes);
				userToUpdate.setFirst_name(user.getFirst_name());
				userToUpdate.setLast_name(user.getLast_name());
				userToUpdate.setDepartment(user.getDepartment());
				userToUpdate.setJob_title(user.getJob_title());
				userToUpdate.setSupervisor(user.getSupervisor());
				userToUpdate.setTown(user.getTown());
				userToUpdate.setCountry(user.getCountry());
				userToUpdate.setPassword(password);
				userToUpdate.setAvatar(user.getAvatar());
				userToUpdate.setUpdated_at(date);
				userRepository.save(userToUpdate);
			}
			/*else if(password != null)
			{
				Date date = new Date();
				/*String folder = "src\\main\\resources\\static\\uploads\\avatars\\";
				byte[] bytes = avatar.getBytes();
				Path path = Paths.get(folder + user.getId());
				Files.write(path , bytes);
				userToUpdate.setFirst_name(user.getFirst_name());
				userToUpdate.setLast_name(user.getLast_name());
				userToUpdate.setDepartment(user.getDepartment());
				userToUpdate.setJob_title(user.getJob_title());
				userToUpdate.setSupervisor(user.getSupervisor());
				userToUpdate.setTown(user.getTown());
				userToUpdate.setCountry(user.getCountry());
				userToUpdate.setPassword(encoder.encode(password));
				userToUpdate.setAvatar(user.getAvatar());
				userToUpdate.setUpdated_at(date);
				userRepository.save(userToUpdate);
			}*/

			return "redirect:/profile?success";
		}
		
		@RequestMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
		@ResponseBody
		public byte[] getImage(Long id) throws IOException {
		    File file = new File("src\\main\\resources\\static\\uploads\\avatars\\"+id);
			return IOUtils.toByteArray(new FileInputStream(file));
		}
		
		@RequestMapping(value = "/kaizen/portal/getImg", produces = MediaType.IMAGE_JPEG_VALUE)
		@ResponseBody
		public byte[] getImg(Long id) throws IOException {
		    File file = new File("src\\main\\resources\\static\\uploads\\avatars\\"+id);
			return IOUtils.toByteArray(new FileInputStream(file));
		}
		@RequestMapping(value = "/kaizen/admin/getImag", produces = MediaType.IMAGE_JPEG_VALUE)
		@ResponseBody
		public byte[] getImag(Long id) throws IOException {
		    File file = new File("src\\main\\resources\\static\\uploads\\avatars\\"+id);
			return IOUtils.toByteArray(new FileInputStream(file));
		}
	  
		/* ----------------------------------- Get users of selected department ----------------------------------- */
		
		@RequestMapping(value = { "/get_user/{department}" }, method = RequestMethod.GET)
		public void GetDepartmentUsers(@PathVariable("department") String department, User user ,HttpServletResponse response) throws IOException {

		    response.setContentType("text/html;charset=UTF-8");
		    PrintWriter out = response.getWriter();
		    List<User> users = userRepository.GetUsersByDepartment(department);
		    try {

		        if (users.isEmpty()) {
		            out.println("No Data Found");
		        }
		        else{
		        	for (User us : users) {
	 	    		out.println(us.getEmail());
	 	    		//out.println(us.getId());
		        	}
		        }
		        out.println();



		    } catch (Exception ex) {

		        out.println("Error ->" + ex.getMessage());

		    } finally {
		        out.close();
		    }

		}
		
		/* ----------------------------------- Get users of selected department ----------------------------------- */
}
