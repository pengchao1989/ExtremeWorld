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
	
	private static final String PAGE_SIZE = "100";
	
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
		
		
		//构建geohash块
		GeoHash centerGeoHash = GeoHash.withCharacterPrecision(latitude, longitude, 12);	//中心
		
		GeoHash northGeoHash = centerGeoHash.getNorthernNeighbour();				//北
		GeoHash eastGeoHash = centerGeoHash.getEasternNeighbour();					//东
		GeoHash southGeoHash = centerGeoHash.getSouthernNeighbour();				//南
		GeoHash westGeoHash = centerGeoHash.getWesternNeighbour();					//西
		
		GeoHash northwestGeoHash = westGeoHash.getNorthernNeighbour();				//西北
		GeoHash northeastGeoHash = eastGeoHash.getNorthernNeighbour();				//东北
		GeoHash southwestGeoHash = westGeoHash.getSouthernNeighbour();				//西南
		GeoHash southeastGeoHash = eastGeoHash.getSouthernNeighbour();				//东南
		
		//更新用户最后的geohash
		if(pageNumber == 1)
		{
			User user = userService.getUser(getCurrentUserId());
			user.setGeoHash(geoHashString);
			user.setLatitude(latitude);
			user.setLongitude(longitude);
			user.setGeoModifyTime(new Date());
			userService.saveUser(user);
		}

		final int GEO_SEARCH_LENGTH = 3;
		String centerGeoHashCode = centerGeoHash.toBase32().substring(0, GEO_SEARCH_LENGTH) + "%";
		String northGeoHashCode = northGeoHash.toBase32().substring(0, GEO_SEARCH_LENGTH) + "%";
		String eastGeoHashCode = eastGeoHash.toBase32().substring(0, GEO_SEARCH_LENGTH) + "%";
		String southGeoHashCode = southGeoHash.toBase32().substring(0, GEO_SEARCH_LENGTH) + "%";
		String westGeoHashCode = westGeoHash.toBase32().substring(0, GEO_SEARCH_LENGTH) + "%";
		String northwestGeoHashCode = northwestGeoHash.toBase32().substring(0, GEO_SEARCH_LENGTH) + "%";
		String northeastGeoHashCode = northeastGeoHash.toBase32().substring(0, GEO_SEARCH_LENGTH) + "%";
		String southwestGeoHashCode = southwestGeoHash.toBase32().substring(0, GEO_SEARCH_LENGTH) + "%";
		String southeastGeoHashCode = southeastGeoHash.toBase32().substring(0, GEO_SEARCH_LENGTH) + "%";
		
		Page<User> userPage = userService.findByGeoHash(
				centerGeoHashCode,
				northGeoHashCode,
				eastGeoHashCode, 
				southGeoHashCode,
				westGeoHashCode,
				northwestGeoHashCode,
				northeastGeoHashCode,
				southwestGeoHashCode,
				southeastGeoHashCode,
				pageNumber,
				pageSize, sortType);//geoHash前几个字符
		
		
		MyPage<UserGeoDTO, User> userMinePage = new MyPage<UserGeoDTO, User>(UserGeoDTO.class,userPage );
		
		WGS84Point centerPoint = new WGS84Point(latitude, longitude);
		for(UserGeoDTO userItem : userMinePage.getContents())
		{
			//GeoHash nearGeoHash  = GeoHash.fromGeohashString(userItem.getGeoHash());
			
			WGS84Point nearPoint = new WGS84Point(userItem.getLatitude(), userItem.getLongitude());
			
			double distence = LantitudeLongitudeDist(userItem.getLongitude(), userItem.getLatitude(),longitude, latitude);
			userItem.setDistance(distence);
			
			System.out.println("geoHash=" + userItem.getGeoHash() +  " userId=" + userItem.getId() +  "distence=" + distence);
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
			user.setCountry(location.getCountry());
			user.setProvince(location.getProvince());
			user.setCity(location.getCity());
			user.setDistrict(location.getDistrict());
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
	
	private static final  double EARTH_RADIUS = 6378137;
	
	private static double rad(double d)  
    {  
       return d * Math.PI / 180.0;  
    }  
	
	public static double LantitudeLongitudeDist(double lon1, double lat1,double lon2, double lat2) {  
        double radLat1 = rad(lat1);  
        double radLat2 = rad(lat2);  
  
        double radLon1 = rad(lon1);  
        double radLon2 = rad(lon2);  
  
        if (radLat1 < 0)  
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south  
        if (radLat1 > 0)  
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north  
        if (radLon1 < 0)  
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west  
        if (radLat2 < 0)  
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south  
        if (radLat2 > 0)  
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north  
        if (radLon2 < 0)  
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west  
        double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1);  
        double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);  
        double z1 = EARTH_RADIUS * Math.cos(radLat1);  
  
        double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);  
        double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);  
        double z2 = EARTH_RADIUS * Math.cos(radLat2);  
  
        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)+ (z1 - z2) * (z1 - z2));  
        //余弦定理求夹角  
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));  
        double dist = theta * EARTH_RADIUS;  
        return dist;  
    }
}
