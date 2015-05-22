package com.yumfee.extremeworld.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.proto.CountryProto;
import com.yumfee.extremeworld.rest.dto.CountryDTO;

@RestController
@RequestMapping(value = "/api/v1/country")
public class CountryRestController {

	@RequestMapping(method = RequestMethod.GET)
	CountryProto.CountryDTO get()
	{
		return CountryProto.CountryDTO.newBuilder()
				.setId("CN")
				.setName("china")
				.setNameZH("zhongguo")
				.build();
		
/*		CountryDTO dto = new CountryDTO();
		dto.setName("china");
		dto.setNameZH("zhongguo");
		
		return dto;*/
	}
}
