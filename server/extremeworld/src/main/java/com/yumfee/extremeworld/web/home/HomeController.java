package com.yumfee.extremeworld.web.home;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.service.ReplyService;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.UserService;

@Controller
@RequestMapping(value = "/")
public class HomeController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private ReplyService replyService;
	
	private static final String PAGE_SIZE = "10";
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
	{
		Page<Topic> topics = topicService.getAllTopic(pageNumber, pageSize, sortType);
		
		//Page<Topic> topics = topicService.getTopicByfollowings(2L,pageNumber, pageSize, sortType);
		
		model.addAttribute("topics", topics);
		
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
