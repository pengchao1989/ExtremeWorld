package com.yumfee.extremeworld.web.home;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.service.UserService;

@Controller
@RequestMapping(value = "/")
public class HomeController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String home(){
			
			
		return "/home/home";
	}


	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String user(@PathVariable("id") Long id, 
			Model model, ServletRequest request)
	{
		
		User user = userService.getUser(id);
		
		model.addAttribute("user", user);
		return "/home/user";
	}
}
