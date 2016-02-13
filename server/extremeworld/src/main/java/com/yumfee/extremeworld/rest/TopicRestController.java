package com.yumfee.extremeworld.rest;


import javax.validation.Validator;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import com.yumfee.extremeworld.config.TopicType;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.TopicDTO;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.UserService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;


@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/topic")
public class TopicRestController
{
	private static Logger logger = LoggerFactory.getLogger(TopicRestController.class);
			
	private static final String PAGE_SIZE = "20";
	
	@Autowired
	TopicService topicService;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping( method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public  MyResponse list(
			@PathVariable String hobby,
			@RequestParam (value = "type", defaultValue = "all") String type,
			@RequestParam (value = "magicType", defaultValue = "") String magicType,
			@RequestParam (value = "courseId", defaultValue = "0") long courseId,
			@RequestParam(value = "taxonomyId", defaultValue = "0") Long taxonomyId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType)
	{
		
		long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		
		Page<Topic> topicPageSource = null;
		
		switch(type)
		{
		case TopicType.ALL:
			topicPageSource = topicService.getTopicByHobby(hobbyId, pageNumber, pageSize, sortType);
			break;
		case TopicType.DISCUSS:
		case TopicType.NEWS:
		case TopicType.VIDEO:
		case TopicType.S_VIDEO:
			if(0 == taxonomyId){
				topicPageSource = topicService.getTopicByHobbyAndType(hobbyId, type, pageNumber, pageSize, sortType);
			}else{
				topicPageSource = topicService.getTopicByHobbyAndTypeAndTaxonomy(hobbyId, type, taxonomyId, pageNumber, pageSize, sortType);
			}
			break;
		case TopicType.COURSE:
			topicPageSource = topicService.getAllTopicByCourseAndMagicType(courseId, magicType, pageNumber, pageSize, sortType);
			break;
		}
		
		MyPage<TopicDTO, Topic> topicPage = new MyPage<TopicDTO, Topic>(TopicDTO.class, topicPageSource);
		
		return MyResponse.ok(topicPage,true);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse get(@PathVariable("id") Long id)
	{
		Topic topic = topicService.getTopic(id);
		if(topic == null)
		{
			String message = "话题不存在(id:" + id + ")";
			logger.warn(message);
			throw new RestException(HttpStatus.NOT_FOUND, message);
		}
		
		TopicDTO topicDto = BeanMapper.map(topic, TopicDTO.class);
		
		

		return MyResponse.ok(topicDto,true);
	}
	
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getTopicByUser(@PathVariable("id") Long userId,
			@RequestParam (value = "type", defaultValue = "all") String type,
			@RequestParam(value = "taxonomyId", defaultValue = "0") Long taxonomyId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType)
	{
		Page<Topic> topicPage = topicService.getTopicByUser(userId, pageNumber, pageSize, sortType);
		
		MyPage<TopicDTO, Topic> myTopicPage = new MyPage<TopicDTO, Topic>(TopicDTO.class, topicPage);
		
	
		return MyResponse.ok(myTopicPage,true);
	}
	
	@RequestMapping(value = "/fine", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getTopicOfFine(
			@PathVariable String hobby,
			@RequestParam (value = "type", defaultValue = "all") String type,
			@RequestParam(value = "taxonomyId", defaultValue = "0") Long taxonomyId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType)
	{
		long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		
		Page<Topic> topicPage = topicService.getFineTopic(hobbyId,pageNumber, pageSize, sortType);
		
		MyPage<TopicDTO, Topic> myTopicPage = new MyPage<TopicDTO, Topic>(TopicDTO.class, topicPage);
		
		return MyResponse.ok(myTopicPage,true);
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
	

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
