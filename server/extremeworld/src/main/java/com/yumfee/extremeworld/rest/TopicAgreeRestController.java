package com.yumfee.extremeworld.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.TopicDTO;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.UserService;

@RestController
@RequestMapping(value = "/api/v1/{hobby}/topic_agree")
public class TopicAgreeRestController
{
	private static Logger logger = LoggerFactory.getLogger(TopicAgreeRestController.class);

	@Autowired
	TopicService topicService;
	
	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON)
	public MyResponse agree(
			@PathVariable String hobby,
			@PathVariable("id") Long id,
			@RequestParam(value = "userId", defaultValue = "1") Long userId)
	{
		
		logger.debug("TopicAgreeRestController get");
		Topic topic = topicService.getTopic(id);
		
		User user = userService.getUser(userId);
		
		List<User> agreeUsers = topic.getAgrees();
		if(agreeUsers != null)
		{
			if( !agreeUsers.contains(user) )
			{
				agreeUsers.add(user);
				topic.setAgreeCount(topic.getAgreeCount() + 1);
			}
			
		}
		
		topicService.saveTopic(topic);
		
		return MyResponse.ok(null);
	}
}
