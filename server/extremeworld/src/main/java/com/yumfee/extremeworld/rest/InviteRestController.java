package com.yumfee.extremeworld.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Invite;
import com.yumfee.extremeworld.rest.dto.*;
import com.yumfee.extremeworld.service.InviteService;

@RestController
@RequestMapping(value = "/api/v1/invite")
public class InviteRestController{
	
	@Autowired
	private InviteService inviteService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getInviter(@RequestParam(value = "phone") String phone){
		
		Invite invite = inviteService.findByPhone(phone);
		
		if(invite != null){
			InviteDTO inviteDTO = BeanMapper.map(invite, InviteDTO.class);
			return MyResponse.ok(inviteDTO);
		}
		
		return MyResponse.ok(null);
		
		
	}
}
