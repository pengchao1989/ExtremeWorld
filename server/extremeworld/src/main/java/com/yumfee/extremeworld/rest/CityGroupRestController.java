package com.yumfee.extremeworld.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.CityGroup;
import com.yumfee.extremeworld.rest.dto.CityGroupDTO;
import com.yumfee.extremeworld.service.CityGroupService;

@RestController
@RequestMapping(value = "/api/v1/city_group")
public class CityGroupRestController
{
	private static Logger logger = LoggerFactory.getLogger(CityGroupRestController.class);
	
	@Autowired
	CityGroupService cityGroupService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	List<CityGroupDTO> list()
	{
		List<CityGroup> cityGroupEntitys = cityGroupService.getAll();
		
		List<CityGroupDTO> cityGroupDTOs = BeanMapper.mapList(cityGroupEntitys, CityGroupDTO.class);
		
		return cityGroupDTOs;
	}
	
}
