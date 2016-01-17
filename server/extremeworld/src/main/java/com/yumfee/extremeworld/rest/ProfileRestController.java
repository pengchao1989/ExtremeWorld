package com.yumfee.extremeworld.rest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.config.ImageHistoryType;
import com.yumfee.extremeworld.config.MyErrorCode;
import com.yumfee.extremeworld.entity.ImageHistory;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.UserDTO;
import com.yumfee.extremeworld.rest.dto.request.UserAttributeRequestDTO;
import com.yumfee.extremeworld.service.ImageHistoryService;
import com.yumfee.extremeworld.service.UserService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@RestController
@RequestMapping(value = "/api/secure/v1/profile")
public class ProfileRestController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ImageHistoryService imageHistoryService;

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
		return MyResponse.ok(userDTO,true);
	}
	
	@RequestMapping(value = "/update_attribute", method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse updateAttribute(@RequestBody UserAttributeRequestDTO attribute, UriComponentsBuilder uriBuilder){
		
		User user = userService.getUser(getCurrentUserId());
		
		if(user == null){
			return MyResponse.err(MyErrorCode.NO_USER);
		}
		
		String fieldName = attribute.getAttributeName();
		if(fieldName != null){
			if(fieldName.equals("nickName")){
				
			}else if(fieldName.equals("gender")){
				user.setGender(attribute.getAttributeValue());
				
			}else if(fieldName.equals("signature")){
				user.setSignature(attribute.getAttributeValue());
			}else if(fieldName.equals("bg")){
				ImageHistory imageHistory = new ImageHistory();
				imageHistory.setKey(attribute.getAttributeValue());
				imageHistory.setType(ImageHistoryType.USER_BACKGROUND);
				imageHistory.setUserId(user.getId());
				imageHistoryService.save(imageHistory);
				user.setBg(attribute.getAttributeValue());
			}else if(fieldName.equals("avatar")){
				ImageHistory imageHistory = new ImageHistory();
				imageHistory.setKey(attribute.getAttributeValue());
				imageHistory.setType(ImageHistoryType.USER_AVATAR);
				imageHistory.setUserId(user.getId());
				imageHistoryService.save(imageHistory);
				user.setAvatar(attribute.getAttributeValue());
			}
		}

		userService.saveUser(user);
		UserDTO userDTO = BeanMapper.map(user, UserDTO.class);
		
		
		return MyResponse.ok(userDTO,true);
	}
	
	@RequestMapping(value = "/update_bg", method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse updateBackground(@RequestBody User userParam, UriComponentsBuilder uriBuilder){
		
    	User user = userService.getUser(userParam.getId());
    	user.setBg(userParam.getBg());
		
		userService.saveUser(user);
		UserDTO userDTO = BeanMapper.map(user, UserDTO.class);
		return MyResponse.ok(userDTO,true);
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
	
}
