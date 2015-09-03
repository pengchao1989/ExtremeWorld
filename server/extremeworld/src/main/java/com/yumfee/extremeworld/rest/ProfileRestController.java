package com.yumfee.extremeworld.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.config.MyErrorCode;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.UserDTO;
import com.yumfee.extremeworld.service.UserService;

@RestController
@RequestMapping(value = "/api/v1/profile")
public class ProfileRestController {
	
	@Autowired
	UserService userService;

	@RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse update(@RequestBody User userParam, UriComponentsBuilder uriBuilder){
		
		User sameNameUser = userService.findUserByName(userParam.getName());
    	if(sameNameUser != null && sameNameUser.getId() != userParam.getId()){
    		return MyResponse.err(MyErrorCode.NAME_REPEAT);
    	}
    	
    	User user = userService.getUser(userParam.getId());
    	user.setName(userParam.getName());
    	user.setAvatar(userParam.getAvatar());
    	user.setSignature(userParam.getSignature());
    	user.setGender(userParam.getGender());
		
		userService.saveUser(user);
		UserDTO userDTO = BeanMapper.map(user, UserDTO.class);
		return MyResponse.ok(userDTO);
	}
	
}
