package com.yumfee.extremeworld.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.service.topic.TopicService;


@RestController
@RequestMapping(value = "/api/v1/topic")
public class TopicRestController
{
	@Autowired
	TopicService topicService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public List<Topic> list()
	{
		return topicService.getAllTopic();
	}
}
