package com.yumfee.extremeworld.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.config.HobbyPathConfig;
import com.yumfee.extremeworld.entity.AppVersion;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.service.AppVersionService;

@RestController
@RequestMapping(value = "/api/v1/{hobby}/check_version")
public class CheckVersionRestController {

	@Autowired
	AppVersionService appVersionService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse check_version(@PathVariable String hobby,
			@RequestParam(value = "currentVersion", defaultValue = "0") String currentVersion){
		
		Long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		AppVersion appVersion = appVersionService.getAppVersion(hobbyId);
		
		return MyResponse.ok(appVersion);
	}
}
