package com.yumfee.extremeworld.rest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.config.HobbyPathConfig;
import com.yumfee.extremeworld.entity.UserScore;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.UserScoreDTO;
import com.yumfee.extremeworld.service.UserScoreService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/ranking_list")
public class RankingListRestController {

	private static final String PAGE_SIZE = "20";
	
	private static Logger logger = LoggerFactory.getLogger(RankingListRestController.class);
	
	@Autowired
	private UserScoreService userScoreService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(@PathVariable String hobby,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		Long hobbyId = HobbyPathConfig.getHobbyId(hobby);

		Page<UserScore> userScorePage = userScoreService.getRankingListByHobbyId(pageNumber, pageSize,sortType, hobbyId);
		
		MyPage<UserScoreDTO, UserScore> userScoreMyPage = new MyPage<UserScoreDTO, UserScore>(UserScoreDTO.class, userScorePage);
		
		return MyResponse.ok(userScoreMyPage, false);
		
	}
	
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
