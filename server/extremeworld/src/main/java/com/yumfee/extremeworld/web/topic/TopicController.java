package com.yumfee.extremeworld.web.topic;

import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.service.TopicService;

@Controller
@RequestMapping(value = "/topic")
public class TopicController
{
	private static final String PAGE_SIZE = "3";
	
	@Autowired
	private TopicService topicService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
	{
		Page<Topic> topics = topicService.getAllTopic(pageNumber, pageSize, sortType);
		
		model.addAttribute("topics", topics);
		return "topic/topicList";
	}
}
