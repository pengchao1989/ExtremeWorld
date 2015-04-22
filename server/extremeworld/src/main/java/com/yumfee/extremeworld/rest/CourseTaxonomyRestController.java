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

import com.yumfee.extremeworld.rest.dto.CourseTaxonomyDTO;
import com.yumfee.extremeworld.service.CourseTaxonomyService;

@RestController
@RequestMapping(value = "/api/v1/course_taxonomy")
public class CourseTaxonomyRestController
{
	private static Logger logger = LoggerFactory.getLogger(CourseTaxonomyRestController.class);
	
	@Autowired
	CourseTaxonomyService courseTaxonomy;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public List<CourseTaxonomyDTO> list()
	{
		List<CourseTaxonomyDTO> courseTaxonomyList = BeanMapper.mapList(courseTaxonomy.getAll(), CourseTaxonomyDTO.class); 
		
		return courseTaxonomyList;
	}
}
