package com.kaizen.project.controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.kaizen.project.model.User;
import com.kaizen.project.services.DepartmentService;

@Controller
public class AuthentificationController {
	
	@Autowired
	private DepartmentService departmentService;
	
	/* Login Route */
	
	public AuthentificationController(DepartmentService departmentService) {
		super();
		this.departmentService = departmentService;
	}

	@GetMapping("/login")
    public String login(Model model) {
        return "kaizen/auth/login";
    }
  
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){   
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }
		
	/* Register Route */
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView showRegister(ModelMap modelMap)
	{
		ModelAndView mav = new ModelAndView();
		User user = new User();
		mav.addObject("user", user);
		mav.addObject("departments", departmentService.getAllDepartments());
		mav.setViewName("kaizen/auth/register"); // resources/templates/register.html
		return mav;
	}
	
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accessDenied(ModelMap modelMap)
	{
		ModelAndView mav = new ModelAndView();
		mav.setViewName("403"); // resources/templates/register.html
		return mav;
	}
	
	@RequestMapping(value = "/kaizen/auth/forgotpassword", method = RequestMethod.GET)
	public ModelAndView ResetPassword(ModelMap modelMap)
	{
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/kaizen/auth/forgotpassword"); // resources/templates/register.html
		return mav;
	}
	
	

	
}
