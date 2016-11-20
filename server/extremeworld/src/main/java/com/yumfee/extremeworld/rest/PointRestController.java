package com.yumfee.extremeworld.rest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Point;
import com.yumfee.extremeworld.entity.Reply;
import com.yumfee.extremeworld.repository.PointDao;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.PointDTO;
import com.yumfee.extremeworld.rest.dto.ReplyDTO;
import com.yumfee.extremeworld.service.PointService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/point")
public class PointRestController {
	
	private static final String PAGE_SIZE = "20";
	
	@Autowired
	PointService pointServer;
	
	@RequestMapping(value="duiba_auto_login", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getAutoLoginUrl(){
		return MyResponse.ok(null);
	}
	
	@RequestMapping(value="points", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getUserPoint(){
		long userId = getCurrentUserId();
		int points = pointServer.getUserTotalPoint(userId);
		return MyResponse.ok(points, true);
	}
	
	@RequestMapping(value="history", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getHistory(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		long userId = getCurrentUserId();
		Page<Point> pointPage = pointServer.getAll(userId, pageNumber, pageSize, sortType);
		
		MyPage<PointDTO, Point> myPointPage = new MyPage<PointDTO, Point>(PointDTO.class, pointPage);
		
		return MyResponse.ok(myPointPage,true);
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}

}
