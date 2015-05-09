package com.yumfee.extremeworld.web.profile;

import java.util.List;

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
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.UserService;

@Controller
@RequestMapping(value = "/u")
public class ProfileController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TopicService topicService;
	
	private static final String PAGE_SIZE = "10";
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String user(@PathVariable("id") Long id, 
			Model model, ServletRequest request)
	{
		
		User user = userService.getUser(id);
		
		Page<Topic> topics = topicService.getTopicByUser(id, 1, 10, "auto");
		//Page<Topic> topics = topicService.getTopicByUserAndMediaWrapNotNull(id);
		
		model.addAttribute("user", user);
		model.addAttribute("topics", topics);
		return "/profile/profile";
	}
	
	@RequestMapping(value = "loadtopic/{id}", method = RequestMethod.GET)
	public String loadMoreOfTopic(@PathVariable("id") Long id,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
	{

		Page<Topic> topics = topicService.getTopicByUser(id, pageNumber, pageSize, sortType);
		
		model.addAttribute("topics", topics);
		return "/home/moreFragment";
	}
	
	@RequestMapping(value = "loadpicture/{id}", method = RequestMethod.GET)
	public String loadMoreOfPicture(@PathVariable("id") Long id, 
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
	{
		
		Page<Topic> topics = topicService.getTopicByUserAndMediaWrapNotNull(id, pageNumber, pageSize, sortType);
		
		model.addAttribute("topics", topics);
		return "/home/moreFragment";
	}
}
