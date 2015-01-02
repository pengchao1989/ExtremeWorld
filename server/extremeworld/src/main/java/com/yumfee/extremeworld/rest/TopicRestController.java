package com.yumfee.extremeworld.rest;

import java.net.URI;
import java.util.List;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.rest.dto.TopicDTO;
import com.yumfee.extremeworld.service.TopicService;


@RestController
@RequestMapping(value = "/api/v1/topic")
public class TopicRestController
{
	private static Logger logger = LoggerFactory.getLogger(TopicRestController.class);
			
	@Autowired
	TopicService topicService;
	
	@Autowired
	private Validator validator;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public List<TopicDTO> list()
	{
		List<TopicDTO> topicDTOlist= BeanMapper.mapList(topicService.getAllTopic(), TopicDTO.class);
		
		return topicDTOlist;
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
	public ResponseEntity<?> create(@RequestBody Topic topic, UriComponentsBuilder uriBuilder)
	{
		//JSR303
		BeanValidators.validateWithException(validator,topic);
		
		topicService.saveTopic(topic);
		
		Long id = topic.getId();
		URI uri = uriBuilder.path("/api/v1/topic/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}
	
}
