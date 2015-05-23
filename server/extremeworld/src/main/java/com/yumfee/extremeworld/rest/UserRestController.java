package com.yumfee.extremeworld.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.UserInfoDTO;
import com.yumfee.extremeworld.service.UserService;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserRestController {

	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse get(@PathVariable("id") Long id)
	{
		User user = userService.getUser(id);
		UserInfoDTO userDTO = BeanMapper.map(user, UserInfoDTO.class);
		return MyResponse.ok(userDTO);
	}
}
