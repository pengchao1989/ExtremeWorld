package com.yumfee.extremeworld.rest;

import java.net.URI;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.config.HobbyPathConfig;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.TopicDTO;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.UserService;


@RestController
@RequestMapping(value = "/api/v1/{hobby}/topic")
public class TopicRestController
{
	private static Logger logger = LoggerFactory.getLogger(TopicRestController.class);
			
	private static final String PAGE_SIZE = "15";
	
	@Autowired
	TopicService topicService;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public  MyResponse list(
			@PathVariable String hobby,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType)
	{
		
		Long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		
		Page<Topic> topicPageSource = topicService.getTopicByHobby(hobbyId, pageNumber, pageSize, sortType);
		
		MyPage<TopicDTO, Topic> topicPage = new MyPage<TopicDTO, Topic>(TopicDTO.class, topicPageSource);
		
		return MyResponse.ok(topicPage);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public TopicDTO get(@PathVariable("id") Long id)
	{
		Topic topic = topicService.getTopic(id);
		if(topic == null)
		{
			String message = "话题不存在(id:" + id + ")";
			logger.warn(message);
			throw new RestException(HttpStatus.NOT_FOUND, message);
		}
		
		TopicDTO topicDto = BeanMapper.map(topic, TopicDTO.class);
		return topicDto;
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse create(@RequestBody Topic topic, UriComponentsBuilder uriBuilder)
	{
		//JSR303
		BeanValidators.validateWithException(validator,topic);
		
		
		
		topicService.saveTopic(topic);
		

		Topic result = topicService.getTopic(topic.getId());
		TopicDTO dto = BeanMapper.map(result, TopicDTO.class);

		return MyResponse.ok(dto);
	}
	

	
}
