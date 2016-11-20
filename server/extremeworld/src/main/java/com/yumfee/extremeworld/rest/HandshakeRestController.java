package com.yumfee.extremeworld.rest;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.config.PointType;
import com.yumfee.extremeworld.entity.Hobby;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.HandshakeDTO;
import com.yumfee.extremeworld.rest.dto.HobbyDTO;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.request.HandshakeRequestDTO;
import com.yumfee.extremeworld.service.BaseInfoService;
import com.yumfee.extremeworld.service.PointService;
import com.yumfee.extremeworld.service.UserService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@RestController
@RequestMapping(value = "/api/v1/handshake")
public class HandshakeRestController {

	private static Logger logger = LoggerFactory.getLogger(HandshakeRestController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	BaseInfoService baseInfoService;
	
	@Autowired
	PointService PointService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	MyResponse get(){
		List<Hobby> hobbys = baseInfoService.getBaseInfo();
		
		List<HobbyDTO> hobbyDTOs = BeanMapper.mapList(hobbys, HobbyDTO.class);
		
		HandshakeDTO handshakeDTO = new HandshakeDTO();
		handshakeDTO.setHobbys(hobbyDTOs);
		return MyResponse.ok(handshakeDTO);
	}
	
	
	@RequestMapping(method = RequestMethod.POST,consumes = MediaTypes.JSON_UTF_8)
	MyResponse post(@RequestBody HandshakeRequestDTO handshakeRequest)
	{
		if(handshakeRequest != null ){
			User user = userService.getUser(handshakeRequest.getUserId());
			if(user != null){
				if (StringUtils.isNotEmpty(handshakeRequest.getHobbyStamp())) {
					user.setHobbyStamp(handshakeRequest.getHobbyStamp());
				}
				if(StringUtils.isNotEmpty(handshakeRequest.getDevice())){
					user.setDevice(handshakeRequest.getDevice());
				}
				if (StringUtils.isNotEmpty(handshakeRequest.getPlateForm())) {
					user.setPlateForm(handshakeRequest.getPlateForm());
				}
				if (StringUtils.isNotEmpty(handshakeRequest.getSystemVersion())) {
					user.setSystemVersion(handshakeRequest.getSystemVersion());
				}
				if (StringUtils.isNotEmpty(handshakeRequest.getVersionCode())) {
					user.setVersionCode(handshakeRequest.getVersionCode());
				}
				if (StringUtils.isNotEmpty(handshakeRequest.getVersionName())) {
					user.setVersionName(handshakeRequest.getVersionName());
				}
				
				//每日登陆积分
				PointService.addPoint(PointType.LOGIN, handshakeRequest.getUserId());
				
				userService.saveUser(user);
			}
		}
		System.out.println("userId=" + handshakeRequest.getUserId());
		System.out.println("hobbyStamp=" + handshakeRequest.getHobbyStamp() );
		
		List<Hobby> hobbys = baseInfoService.getBaseInfo();
		
		List<HobbyDTO> hobbyDTOs = BeanMapper.mapList(hobbys, HobbyDTO.class);
		
		HandshakeDTO handshakeDTO = new HandshakeDTO();
		handshakeDTO.setHobbys(hobbyDTOs);
		return MyResponse.ok(handshakeDTO);
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
