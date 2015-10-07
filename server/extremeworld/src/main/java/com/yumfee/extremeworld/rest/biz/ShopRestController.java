/**
 * 
 */
package com.yumfee.extremeworld.rest.biz;

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
import com.yumfee.extremeworld.entity.biz.Shop;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.biz.ShopDTO;
import com.yumfee.extremeworld.service.biz.ShopService;

/**
 * @author pengchao
 *
 */
@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/biz/shop")
public class ShopRestController {
	private static Logger logger = LoggerFactory.getLogger(ShopRestController.class);
	
	private static final String PAGE_SIZE = "15";
	
	@Autowired
	ShopService shopService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(@PathVariable String hobby,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		Long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		Page<Shop> shopPage = shopService.getAllByHobbyId(hobbyId, pageNumber, pageSize, sortType);
		MyPage<ShopDTO,Shop> myShopPage = new MyPage<ShopDTO,Shop>(ShopDTO.class, shopPage);
		
		return MyResponse.ok(myShopPage);
	}
	

	
}
