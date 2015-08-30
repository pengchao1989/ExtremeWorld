package com.yumfee.extremeworld.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.yumfee.extremeworld.config.MyErrorCode;
import com.yumfee.extremeworld.entity.Country;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.UserDTO;
import com.yumfee.extremeworld.service.account.AccountService;

@RestController
@RequestMapping(value = "/api/v1/{hobby}/account")
public class AccountRestController {

	@Autowired
	AccountService accountService;
	
	
	@RequestMapping(value = "/qqlogin", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse login(
			@PathVariable String hobby,
			@RequestParam(value = "qqOpenId", defaultValue = "0") String qqOpenId
			)
	{
		
		User user = accountService.findUserByQqOpenId(qqOpenId);
		if(user != null)
		{
			UserDTO userDTO = BeanMapper.map(user, UserDTO.class);
			return MyResponse.ok(userDTO);
		}
		
		
		return MyResponse.noContent();
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse register(@PathVariable String hobby,
			@RequestBody User user, UriComponentsBuilder uriBuilder){
		
		String qqOpenId = user.getQqOpenId();
		user.setLoginName(qqOpenId);
		user.setPlainPassword(qqOpenId);
		user.setHobbyStamp(hobby);
		
		String birth = user.getBirth();
		if(birth.length() == 4 )
		{
			birth += "-01-01";
			user.setBirth(birth);
		}else{
			user.setBirth("1995-01-01");
		}
		
		
    	Country country = new Country();
    	country.setId("CN");
    	
    	user.setCountry(country);
    	
    	User sameNameUser = accountService.findUserByName(user.getName());
    	if(sameNameUser != null){
    		return MyResponse.err(MyErrorCode.NAME_REPEAT);
    	}
		
    	accountService.registerUser(user);
		
		UserDTO userDTO = BeanMapper.map(user, UserDTO.class);
		return MyResponse.ok(userDTO);
	}
}
