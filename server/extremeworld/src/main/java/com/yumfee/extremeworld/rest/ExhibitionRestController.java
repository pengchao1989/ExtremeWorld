package com.yumfee.extremeworld.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.config.HobbyPathConfig;
import com.yumfee.extremeworld.entity.Exhibition;
import com.yumfee.extremeworld.rest.dto.ExhibitionDTO;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.service.ExhibitionService;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/exhibition")
public class ExhibitionRestController {

	private static final String PAGE_SIZE = "5";
	
	
	@Autowired
	ExhibitionService exhibitionService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(@PathVariable String hobby,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		Long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		Page<Exhibition> exhibitionPage = exhibitionService.getAllByHobby(hobbyId, pageNumber, pageSize, sortType);
		
		MyPage<ExhibitionDTO, Exhibition> exhibitionMyPage = new MyPage<ExhibitionDTO,Exhibition >(ExhibitionDTO.class, exhibitionPage);
		
		return MyResponse.ok(exhibitionMyPage,false);
	}
	
	
}
