package com.yumfee.extremeworld.rest;

import javax.validation.Validator;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.TopicScore;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.TopicScoreDTO;
import com.yumfee.extremeworld.rest.dto.request.TopicScoreRequestDTO;
import com.yumfee.extremeworld.service.TopicScoreService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/topic_score")
public class TopicScoreRestController {

	private static final String PAGE_SIZE = "10";
	
	private static Logger logger = LoggerFactory.getLogger(TopicScoreRestController.class);
	
	@Autowired
	private TopicScoreService topicScoreService;
	
	@Autowired
	private Validator validator;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(@PathVariable String hobby,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		Page<TopicScore> pageTopicScore = topicScoreService.getAll(pageNumber, pageSize, sortType);
		MyPage<TopicScoreDTO, TopicScore> myPageTopicScore = new MyPage<TopicScoreDTO, TopicScore>(TopicScoreDTO.class, pageTopicScore);
		return MyResponse.ok(myPageTopicScore, false);
	}
	
	@RequestMapping(method = RequestMethod.POST,consumes = MediaTypes.JSON_UTF_8)
	public MyResponse create(@RequestBody TopicScoreRequestDTO topicScoreRequestDTO){
		//JSR303
		BeanValidators.validateWithException(validator,topicScoreRequestDTO);
		
		TopicScore topicScore = new TopicScore();
		topicScore.setScore(topicScoreRequestDTO.getScore());
		Long userId = getCurrentUserId();
		User user = new User();
		user.setId(userId);
		topicScore.setId(userId);
		Topic topic = new Topic();
		topic.setId(topicScoreRequestDTO.getTopicId());
		topicScore.setTopic(topic);
		
		//TODO  判定唯一性
		
		
		topicScoreService.save(topicScore);
		
		TopicScoreDTO topicScoreDTO = BeanMapper.map(topicScore, TopicScoreDTO.class);
		return MyResponse.ok(topicScoreDTO, false);
		
	}
	
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
