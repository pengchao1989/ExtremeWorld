package com.yumfee.extremeworld.web.course;

import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yumfee.extremeworld.entity.Course;
import com.yumfee.extremeworld.entity.CourseTaxonomy;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.service.CourseService;
import com.yumfee.extremeworld.service.CourseTaxonomyService;
import com.yumfee.extremeworld.service.TopicService;

@Controller
@RequestMapping(value = "/course")
public class CourseController
{
	private static final String PAGE_SIZE = "10";
	
	@Autowired
	private CourseTaxonomyService courseTaxonomyService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired 
	private TopicService topicService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request)
	{
		List<CourseTaxonomy> courseTaxonomyList = courseTaxonomyService.getAll();
		
		model.addAttribute("courseTaxonomyList", courseTaxonomyList);
		
		return "/course/courseList";
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") Long id, 
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
	
	{
		
		Course course = courseService.getCourse(id);
		
		Page<Topic> topics = topicService.getAllTopicByCourse(id, pageNumber, pageSize, sortType);
		
		model.addAttribute("course", course);
		model.addAttribute("topics", topics);
		
		return "/course/courseDetail";
	}
}
