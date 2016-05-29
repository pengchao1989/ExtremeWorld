package com.yumfee.extremeworld.rest;

import javax.validation.Validator;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.config.HobbyPathConfig;
import com.yumfee.extremeworld.entity.Site;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.SiteDTO;
import com.yumfee.extremeworld.service.SiteService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/site")
public class SiteRestController
{
	private static final String PAGE_SIZE = "20";
	
	private static Logger logger = LoggerFactory.getLogger(SiteRestController.class);
	
	@Autowired
	SiteService siteService;
	
	@Autowired
	private Validator validator;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(
			@PathVariable String hobby,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType)
	{
		
		long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		
		
		Page<Site> sitePages = siteService.getByHobby(hobbyId, pageNumber, pageSize, sortType);
		
		MyPage<SiteDTO,Site> mySitePages = new MyPage<SiteDTO,Site>(SiteDTO.class, sitePages);
		
		return MyResponse.ok(mySitePages,true);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse create(@RequestBody Site site, UriComponentsBuilder uriBuilder){
		//JSR303
		BeanValidators.validateWithException(validator,site);
		
		Long userId = getCurrentUserId();
		User user = new User();
		user.setId(userId);
		site.setUser(user);
		siteService.saveSite(site);
		
		SiteDTO siteDto = BeanMapper.map(site, SiteDTO.class);
		
		return MyResponse.ok(siteDto);
		
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
