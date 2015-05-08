package com.yumfee.extremeworld.web.user;

import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.UserService;

@Controller
@RequestMapping(value = "/u")
public class UserCOntroller {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TopicService topicService;
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String user(@PathVariable("id") Long id, 
			Model model, ServletRequest request)
	{
		
		User user = userService.getUser(id);
		
		//List<Topic> topics = topicService.getTopicByUser(id);
		List<Topic> topics = topicService.getTopicByUserAndMediaWrapNotNull(id);
		
		model.addAttribute("user", user);
		model.addAttribute("topics", topics);
		return "/profile/profile";
	}
}
