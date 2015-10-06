package com.yumfee.extremeworld.rest.biz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.biz.CategoryPredefined;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.biz.CategoryPredefinedDTO;
import com.yumfee.extremeworld.service.biz.CategoryPredefinedService;

@RestController
@RequestMapping(value = "/api/secure/v1/biz/category_predefined")
public class CategoryPredefinedRestController {
	private static Logger logger = LoggerFactory.getLogger(CategoryPredefinedRestController.class);
	
	@Autowired
	CategoryPredefinedService categoryPredefinedService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	MyResponse list(){
		List<CategoryPredefined> categoryPredifinedList = categoryPredefinedService.getAll();
		
		List<CategoryPredefinedDTO> categoryPredefinedDTOList = BeanMapper.mapList(categoryPredifinedList, CategoryPredefinedDTO.class);
		
		return MyResponse.ok(categoryPredefinedDTOList);
	}
}
