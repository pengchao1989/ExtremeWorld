package com.yumfee.extremeworld.web.course;

import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yumfee.extremeworld.entity.CourseTaxonomy;
import com.yumfee.extremeworld.service.CourseTaxonomyService;

@Controller
@RequestMapping(value = "/course")
public class CourseController
{
	@Autowired
	private CourseTaxonomyService courseTaxonomyService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request)
	{
		List<CourseTaxonomy> courseTaxonomyList = courseTaxonomyService.getAll();
		
		model.addAttribute("courseTaxonomyList", courseTaxonomyList);
		
		return "/course/courseList";
	}
}
