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

import com.yumfee.extremeworld.entity.UserInfo;
import com.yumfee.extremeworld.rest.dto.UserInfoMinDTO;
import com.yumfee.extremeworld.service.UserInfoService;

@RestController
@RequestMapping(value = "/api/v1/follower")
public class FollowerRestController {

	private static Logger logger = LoggerFactory.getLogger(FollowerRestController.class);
	
	@Autowired
	UserInfoService userInfoService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	List<UserInfoMinDTO> list(@PathVariable("id") Long id)
	{
		List<UserInfo> userInfoList = userInfoService.getFollowers(id);
		
		List<UserInfoMinDTO> userInfoDTOList = BeanMapper.mapList(userInfoList, UserInfoMinDTO.class);
		return userInfoDTOList;
	}
}
