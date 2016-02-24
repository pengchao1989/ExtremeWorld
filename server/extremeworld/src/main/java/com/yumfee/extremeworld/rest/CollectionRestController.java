package com.yumfee.extremeworld.rest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Collection;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.CollectionDTO;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.service.CollectionService;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.UserService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@RestController
@RequestMapping(value = "/api/secure/v1/collection")
public class CollectionRestController {
	
	private static Logger logger = LoggerFactory.getLogger(TopicRestController.class);
	
	private static final String PAGE_SIZE = "20";
	
	@Autowired
	private CollectionService collectionService;
	
	@Autowired
	TopicService topicService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping( method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public  MyResponse list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		long userId = getCurrentUserId();
		Page<Collection> collectionPage = collectionService.getCollectionByUser(userId, pageNumber, pageSize, sortType);
		MyPage<CollectionDTO, Collection> collectionMyPage = new MyPage<CollectionDTO, Collection>(CollectionDTO.class, collectionPage);
		
		return MyResponse.ok(collectionMyPage, true);
	}
	
	@RequestMapping(value = "/{topicId}", method = RequestMethod.POST, produces = MediaTypes.JSON)
	public MyResponse collection(
			@PathVariable("topicId") Long topicId){
		
		Topic topic = topicService.getTopic(topicId);
		User user = userService.getUser(getCurrentUserId());
		Collection collection = new Collection();
		collection.setTopic(topic);
		collection.setUser(user);
		
		Collection collectionResult = collectionService.saveCollection(collection);
		
		CollectionDTO collectionDTOResult = BeanMapper.map(collectionResult, CollectionDTO.class);
		
		return MyResponse.ok(collectionDTOResult, true);
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

}
