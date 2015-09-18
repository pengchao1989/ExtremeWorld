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

import com.yumfee.extremeworld.entity.Activity;
import com.yumfee.extremeworld.rest.dto.ActivityDTO;
import com.yumfee.extremeworld.service.ActivityService;

@RestController
@RequestMapping(value = "/api/secure/v1/activity")
public class ActivityRestController
{
	private static Logger logger = LoggerFactory.getLogger(ActivityRestController.class);
	
	@Autowired
	ActivityService activityService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	List<ActivityDTO> list()
	{
		List<Activity> activityEntitys = activityService.getAll();
		
		List<ActivityDTO> activityDTOs = BeanMapper.mapList(activityEntitys, ActivityDTO.class);
		
		return activityDTOs;
	}
}
