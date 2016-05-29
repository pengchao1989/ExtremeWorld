package com.yumfee.extremeworld.rest;

import org.apache.commons.lang3.StringUtils;
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

import com.yumfee.extremeworld.config.MyErrorCode;
import com.yumfee.extremeworld.entity.Country;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.entity.VerificationCode;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.UserDTO;
import com.yumfee.extremeworld.rest.dto.request.AgreeDTO;
import com.yumfee.extremeworld.rest.dto.request.LoginDTO;
import com.yumfee.extremeworld.service.InviteService;
import com.yumfee.extremeworld.service.VerificationCodeService;
import com.yumfee.extremeworld.service.account.AccountService;

@RestController
@RequestMapping(value = "/api/v1/{hobby}/account")
public class AccountRestController {

	@Autowired
	AccountService accountService;
	
	@Autowired
	InviteService inviteService;
	
	@Autowired
	VerificationCodeService verificationCodeService;
	
	
	@RequestMapping(value = "/qq_login", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse qq_login(
			@PathVariable String hobby,
			@RequestParam(value = "qqOpenId", defaultValue = "0") String qqOpenId
			)
	{
		
		User user = accountService.findUserByQqOpenId(qqOpenId);
		if(user != null && user.isInited())
		{
			UserDTO userDTO = BeanMapper.map(user, UserDTO.class);
			return MyResponse.ok(userDTO);
		}
		
		return MyResponse.err(MyErrorCode.NO_USER);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse login(
			@PathVariable String hobby,
			@RequestParam(value = "loginName", defaultValue = "") String loginName,
			@RequestParam(value = "password", defaultValue = "") String password
			)
	{
		
		User user = accountService.findUserByLoginName(loginName);
		if(user != null)
		{
			if(accountService.checkPassword(user, password)){
				UserDTO userDTO = BeanMapper.map(user, UserDTO.class);
				return MyResponse.ok(userDTO);
			}else{
				return MyResponse.err(MyErrorCode.PASSWORD_FAILED);
			}
		}
		
		return MyResponse.err(MyErrorCode.NO_USER);
	}
	

	@RequestMapping(value = "/login", method = RequestMethod.POST,consumes = MediaTypes.JSON)
	public MyResponse login(@PathVariable String hobby,
			@RequestBody LoginDTO loginDTO,
			UriComponentsBuilder uriBuilder){
		User user = accountService.findUserByLoginName(loginDTO.getLoginName());
		if(user != null)
		{
			if(accountService.checkPassword(user, loginDTO.getPassword())){
				UserDTO userDTO = BeanMapper.map(user, UserDTO.class);
				return MyResponse.ok(userDTO);
			}else{
				return MyResponse.err(MyErrorCode.PASSWORD_FAILED);
			}
		}
		
		return MyResponse.err(MyErrorCode.NO_USER);
	}
	
	@RequestMapping(value = "/qq_register", method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse register(@PathVariable String hobby,
			@RequestBody User user, 
			@RequestParam(value = "invitationCode", defaultValue = "1") String invitationCode,
			UriComponentsBuilder uriBuilder){
		
		String qqOpenId = user.getQqOpenId();
		
		User registedUser = accountService.findUserByQqOpenId(qqOpenId);
		
			user.setLoginName(qqOpenId);
			user.setPlainPassword(qqOpenId);
			user.setHobbyStamp(hobby);
			
			String birth = user.getBirth();
			if(birth.length() == 4 ){
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
	    	
	    	user.setInited(true);
	    	
	    	if(registedUser == null){
	    		accountService.registerUser(user);
	    		UserDTO userDTO = BeanMapper.map(user, UserDTO.class);
				return MyResponse.ok(userDTO);
	    	}else
	    	{
	    		registedUser.setAvatar(user.getAvatar());
	    		registedUser.setName(user.getName());
	    		registedUser.setBirth(user.getBirth());
	    		registedUser.setCountry(user.getCountry());
	    		registedUser.setInited(true);
	    		
	    		accountService.updateUser(registedUser);
	    		
	    		UserDTO userDTO = BeanMapper.map(registedUser, UserDTO.class);
				return MyResponse.ok(userDTO);
	    	}
	}
	
	@RequestMapping(value = "/phone_register", method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse phoneRegister(@PathVariable String hobby,
			@RequestBody User user, 
			@RequestParam(value = "verCode", defaultValue = "1") String verCode,
			@RequestParam(value = "invitationCode", defaultValue = "1") String invitationCode,
			UriComponentsBuilder uriBuilder){
		
		String phone = user.getPhone();
		String password = user.getPassword();
		user.setLoginName(phone);
		user.setPhone(phone);
		user.setPlainPassword(password);
		user.setHobbyStamp(hobby);
		user.setInited(true);
		
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
    	
    	
    	VerificationCode verificationCode = verificationCodeService.getVerificationCodeByPhoneAndCode(phone, verCode);
    	
    	if(verificationCode == null){
    		return MyResponse.err(MyErrorCode.VERIFICATION_CODE_ERROR);
    	}
    	
    	if(StringUtils.isEmpty(password)){
    		return MyResponse.err(MyErrorCode.PASSWORD_EMPTY);
    	}
    	
    	if(StringUtils.isBlank(phone)){
    		return MyResponse.err(MyErrorCode.PHONE_EMPTY);
    	}
    	
    	User samePhoneUser = accountService.findByPhone(phone);
    	if(samePhoneUser != null){
    		return MyResponse.err(MyErrorCode.PHONE_REGISTERED);
    	}
    	

		
    	accountService.registerUser(user);
		
		UserDTO userDTO = BeanMapper.map(user, UserDTO.class);
		return MyResponse.ok(userDTO);
	}
}
