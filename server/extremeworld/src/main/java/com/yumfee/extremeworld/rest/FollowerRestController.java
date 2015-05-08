package com.yumfee.extremeworld.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.UserMinDTO;
import com.yumfee.extremeworld.service.UserService;

@RestController
@RequestMapping(value = "/api/v1/follower")
public class FollowerRestController {

	private static Logger logger = LoggerFactory.getLogger(FollowerRestController.class);
	
	@Autowired
	UserService userInfoService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	List<UserMinDTO> list(@PathVariable("id") Long id)
	{
		List<User> userInfoList = userInfoService.getFollowers(id);
		
		List<UserMinDTO> userInfoDTOList = BeanMapper.mapList(userInfoList, UserMinDTO.class);
		return userInfoDTOList;
	}
}
