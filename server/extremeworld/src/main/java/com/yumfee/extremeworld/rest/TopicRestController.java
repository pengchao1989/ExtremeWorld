package com.yumfee.extremeworld.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extrmeworld.rest.dto.TopicDTO;


@RestController
@RequestMapping(value = "/api/v1/topic")
public class TopicRestController
{
	private static Logger logger = LoggerFactory.getLogger(TopicRestController.class);
			
	@Autowired
	TopicService topicService;
	
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
	
}
