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

import com.yumfee.extremeworld.entity.biz.Goods;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.biz.GoodsDTO;
import com.yumfee.extremeworld.service.biz.GoodsService;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/biz/goods")
public class GoodsRestController {
	private static Logger logger = LoggerFactory.getLogger(GoodsRestController.class);
	
	private static final String PAGE_SIZE = "15";
	
	@Autowired
	GoodsService goodsService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	MyResponse list(@PathVariable String hobby,
			@RequestParam(value = "shopId", defaultValue = "1") int shopId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		Page<Goods> goodsPage = goodsService.getAll(pageNumber, pageSize, sortType);
		MyPage<GoodsDTO, Goods> myGoodsPage = new MyPage<GoodsDTO,Goods>(GoodsDTO.class,goodsPage);
		
		return MyResponse.ok(myGoodsPage);
	}
	
	@RequestMapping(value="/shop/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse listByShop(@PathVariable String hobby,
			@PathVariable("id") Long id,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		Page<Goods> goodsPage = goodsService.getByShoopId(id, pageNumber, pageSize, sortType);
		MyPage<GoodsDTO, Goods> myGoodsPage = new MyPage<GoodsDTO,Goods>(GoodsDTO.class,goodsPage);
		
		return MyResponse.ok(myGoodsPage);
	}
	
	@RequestMapping(value="/category/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse listByCategory(@PathVariable String hobby,
			@PathVariable("id") Long id,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		Page<Goods> goodsPage = goodsService.getByCategoryId(id, pageNumber, pageSize, sortType);
		MyPage<GoodsDTO, Goods> myGoodsPage = new MyPage<GoodsDTO,Goods>(GoodsDTO.class,goodsPage);
		
		return MyResponse.ok(myGoodsPage);
	}
	
	
}
