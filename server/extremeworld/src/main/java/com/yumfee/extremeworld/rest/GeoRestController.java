package com.yumfee.extremeworld.rest;


import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.queries.GeoHashCircleQuery;

import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.request.PointDTO;
import com.yumfee.extremeworld.service.UserService;

@RestController
@RequestMapping(value = "/api/v1/{hobby}/geo")
public class GeoRestController {
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(@PathVariable String hobby) {
		
		WGS84Point center = new WGS84Point(39.86391280373075, 116.37356590048701);
		GeoHashCircleQuery query = new GeoHashCircleQuery(center, 589);
		
		// the distance between center and test1 is about 430 meters
		WGS84Point test1 = new WGS84Point(39.8648866576058, 116.378465869303);
		// the distance between center and test2 is about 510 meters
		WGS84Point test2 = new WGS84Point(39.8664787092599, 116.378552856158);
		
		WGS84Point point = new WGS84Point(39.86391280373075, 116.37356590048701);
		
		String geoHashString = GeoHash.geoHashStringWithCharacterPrecision(point.getLatitude(), point.getLongitude(), 12);
		
		return MyResponse.ok(geoHashString);
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
