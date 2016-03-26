package com.yumfee.extremeworld.rest;


import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.VincentyGeodesy;

import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.UserGeoDTO;
import com.yumfee.extremeworld.rest.dto.request.LocationDTO;
import com.yumfee.extremeworld.service.UserService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/geo")
public class GeoRestController {
	
	private static final String PAGE_SIZE = "50";
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/near_friend", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(@PathVariable String hobby,
			@RequestParam (value = "userId", defaultValue = "0") Long userId,
			@RequestParam (value = "latitude", defaultValue = "0") double latitude,
			@RequestParam (value = "longitude", defaultValue = "0") double longitude,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType
			) {
		
		WGS84Point center = new WGS84Point(latitude, longitude);
		
		String geoHashString = GeoHash.geoHashStringWithCharacterPrecision(center.getLatitude(), center.getLongitude(), 12);
		
		System.out.println("latitude=" + center.getLatitude() + "longitude" + center.getLongitude() + "  geoHash=" + geoHashString);
		
		//更新用户最后的geohash
		if(pageNumber == 1)
		{
			User user = userService.getUser(userId);
			user.setGeoHash(geoHashString);
			user.setGeoModifyTime(new Date());
			userService.saveUser(user);
		}

		
		Page<User> userPage = userService.findByGeoHash(geoHashString.substring(0, 2) + "%",pageNumber, pageSize, sortType);//geoHash前几个字符
		
		
		MyPage<UserGeoDTO, User> userMinePage = new MyPage<UserGeoDTO, User>(UserGeoDTO.class,userPage );
		
		for(UserGeoDTO userItem : userMinePage.getContents())
		{
			GeoHash nearGeoHash  = GeoHash.fromGeohashString(userItem.getGeoHash());
			
			WGS84Point nearPoint = nearGeoHash.getPoint();
			WGS84Point centerPoint = new WGS84Point(latitude, longitude);
			
			
			double distence = VincentyGeodesy.distanceInMeters(centerPoint, nearPoint);
			userItem.setDistance(distence);
			
			System.out.println("userId=" + userItem.getId() +  "distence=" + distence);
		}
		
		//排序
		Collections.sort(userMinePage.getContents(), new Comparator<UserGeoDTO>(){

			@Override
			public int compare(UserGeoDTO o1, UserGeoDTO o2) {
				
				return (int) (o1.getDistance() - o2.getDistance());
			}
			
		});
		
		
		return MyResponse.ok(userMinePage,true);
	}
	
	@RequestMapping(value = "/publish_location", method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse updatePoint(@RequestBody LocationDTO location){
		
		User user = userService.getUser(getCurrentUserId());
		
		WGS84Point center = new WGS84Point(location.getLatitude(), location.getLongitude());

		String geoHash = GeoHash.geoHashStringWithCharacterPrecision(center.getLatitude(), center.getLongitude(), 12);
		
		user.setLatitude(location.getLatitude());
		user.setLongitude(location.getLongitude());
		if(StringUtils.isNotEmpty(location.getAddress())){
			user.setAddress(location.getAddress());
		}
		user.setGeoHash(geoHash);
		user.setGeoModifyTime(new Date());
		
		userService.saveUser(user);
		
		return MyResponse.ok(null);
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
