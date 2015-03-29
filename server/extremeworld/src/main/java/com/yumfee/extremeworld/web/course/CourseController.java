package com.yumfee.extremeworld.web.course;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yumfee.extremeworld.entity.Course;
import com.yumfee.extremeworld.entity.CourseTaxonomy;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.service.CourseService;
import com.yumfee.extremeworld.service.CourseTaxonomyService;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

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
	
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model)
	{
		List<CourseTaxonomy> courseTaxonomyList = courseTaxonomyService.getAll();
		model.addAttribute("courseTaxonomyList", courseTaxonomyList);
		
		
		model.addAttribute("course", new Course());
		model.addAttribute("action", "create");
		
		
		return "/course/courseForm";
	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Course newCourse, RedirectAttributes redirectAttributes,ServletRequest request)
	{
		System.out.println("create");
		System.out.println("courseTaxonomyId" + request.getParameter("courseTaxonomyId"));

		Long courseTaxonomyId = Long.valueOf(request.getParameter("courseTaxonomyId"));
		
		User user = new User(getCurrentUserId());
		newCourse.setUser(user);
		
		CourseTaxonomy courseTaxonomy = new CourseTaxonomy();
		courseTaxonomy.setId(courseTaxonomyId);
		
		newCourse.setCourseTaxonomy(courseTaxonomy);
		
		courseService.saveCourse(newCourse);
		
		redirectAttributes.addFlashAttribute("message", "创建教学成功");
		
		return "redirect:/course/";
	}
	
	
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model)
	{
		model.addAttribute("course", courseService.getCourse(id));
		model.addAttribute("action", "update");
		return "/course/courseForm";
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("course") Course course, RedirectAttributes redirectAttributes)
	{
		return "redirect:/course/";
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
