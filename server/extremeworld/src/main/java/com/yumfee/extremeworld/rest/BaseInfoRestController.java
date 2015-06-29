package com.yumfee.extremeworld.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Hobby;
import com.yumfee.extremeworld.rest.dto.BaseInfoDTO;
import com.yumfee.extremeworld.rest.dto.HobbyDTO;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.service.BaseInfoService;

@RestController
@RequestMapping(value = "/api/v1/base_info")
public class BaseInfoRestController {


	@Autowired
	BaseInfoService baseInfoService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	MyResponse get()
	{
		List<Hobby> hobbys = baseInfoService.getBaseInfo();
		
		List<HobbyDTO> hobbyDTOs = BeanMapper.mapList(hobbys, HobbyDTO.class);
		
		BaseInfoDTO baseInfoDTO = new BaseInfoDTO();
		baseInfoDTO.setHobbys(hobbyDTOs);
		return MyResponse.ok(baseInfoDTO);
	}
}
