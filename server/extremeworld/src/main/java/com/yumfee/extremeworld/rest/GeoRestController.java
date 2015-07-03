package com.yumfee.extremeworld.rest;


import java.util.List;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.queries.GeoHashCircleQuery;
import ch.hsr.geohash.util.VincentyGeodesy;

import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.UserMinDTO;
import com.yumfee.extremeworld.rest.dto.request.PointDTO;
import com.yumfee.extremeworld.service.UserService;

@RestController
@RequestMapping(value = "/api/v1/{hobby}/geo")
public class GeoRestController {
	
	private static final String PAGE_SIZE = "30";
	
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
		
		//更新用户最后的geohash
		if(pageNumber == 1)
		{
			User user = userService.getUser(userId);
			user.setGeoHash(geoHashString);
			userService.saveUser(user);
		}

		
		
		Page<User> userPage = userService.findByGeoHash(geoHashString.substring(0, 4) + "%",pageNumber, pageSize, sortType);//geoHash前几个字符
		
		
		MyPage<UserMinDTO, User> userMinePage = new MyPage<UserMinDTO, User>(UserMinDTO.class,userPage );
		
		for(UserMinDTO userItem : userMinePage.getContents())
		{
			GeoHash nearGeoHash  = GeoHash.fromGeohashString(userItem.getGeoHash());
			
			WGS84Point nearPoint = nearGeoHash.getPoint();
			WGS84Point centerPoint = new WGS84Point(latitude, longitude);
			
			
			double distence = VincentyGeodesy.distanceInMeters(centerPoint, nearPoint);
			userItem.setDistance(distence);
			
			System.out.println("userId=" + userItem.getId() +  "distence=" + distence);
		}
		
		
		
		return MyResponse.ok(userMinePage);
	}
	
	@RequestMapping(value = "/update_user_point", method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse updatePoint(@RequestBody PointDTO point)
	{
		User user = userService.getUser(point.getId());
		
		WGS84Point center = new WGS84Point(point.getLatitude(), point.getLongitude());

		String geoHash = GeoHash.geoHashStringWithCharacterPrecision(center.getLatitude(), center.getLongitude(), 12);
		
		user.setGeoHash(geoHash);
		
		userService.saveUser(user);
		
		return MyResponse.ok(null);
	}
}
