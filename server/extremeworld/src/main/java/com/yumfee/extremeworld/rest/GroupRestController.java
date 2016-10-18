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
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.config.HobbyPathConfig;
import com.yumfee.extremeworld.entity.Group;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.GroupDTO;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.service.GroupService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/group")
public class GroupRestController {

	private static final String PAGE_SIZE = "20";
	private static Logger logger = LoggerFactory.getLogger(GroupRestController.class);
	
	@Autowired
	GroupService groupService;
	
	@Autowired
	private Validator validator;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(
			@PathVariable String hobby,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType)
	{
		long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		
		Page<Group> groupPage = groupService.getByHobby(hobbyId, pageNumber,pageSize, sortType);
		
		MyPage<GroupDTO, Group> myGroupPage = new MyPage<GroupDTO, Group>(GroupDTO.class, groupPage);
		
		return MyResponse.ok(myGroupPage);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse create(@PathVariable String hobby,@RequestBody Group group, UriComponentsBuilder uriBuilder){
		BeanValidators.validateWithException(validator, group);
		
		//user
		Long userId = getCurrentUserId();
		User user = new User();
		user.setId(userId);
		group.setUser(user);
		
		//hobby
		long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		
		//location
		WGS84Point center = new WGS84Point(group.getLatitude(), group.getLongitude());
		String geoHash = GeoHash.geoHashStringWithCharacterPrecision(center.getLatitude(), center.getLongitude(), 12);
		group.setGetHash(geoHash);
		
		
		GroupDTO groupDTO = BeanMapper.map(group, GroupDTO.class);
		
		return MyResponse.ok(groupDTO);
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
