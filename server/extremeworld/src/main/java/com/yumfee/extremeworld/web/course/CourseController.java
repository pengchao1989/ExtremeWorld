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
@RequestMapping(value = "{hobby}/course")
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
	public String list(
			@PathVariable String hobby,
			Model model, 
			ServletRequest request)
	{
		List<CourseTaxonomy> courseTaxonomyList = courseTaxonomyService.getAll();
		
		model.addAttribute("courseTaxonomyList", courseTaxonomyList);
		
		return "/course/courseList";
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String detail(
			@PathVariable String hobby,
			@PathVariable("id") Long id, 
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
	
	{
		
		Course course = courseService.getCourse(id);
		
		Page<Topic> topics = topicService.getAllTopicByCourse(id, pageNumber, pageSize, sortType);
		
		model.addAttribute("course", course);
		model.addAttribute("topics", topics);
		
		model.addAttribute("hobby", hobby);
		return "/course/courseDetail";
	}
	
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(
			@PathVariable String hobby,
			Model model)
	{
		List<CourseTaxonomy> courseTaxonomyList = courseTaxonomyService.getAll();
		model.addAttribute("courseTaxonomyList", courseTaxonomyList);
		
		
		model.addAttribute("course", new Course());
		model.addAttribute("action", "create");
		
		model.addAttribute("hobby", hobby);
		
		return "/course/courseForm";
	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(
			@PathVariable String hobby,
			@Valid Course newCourse, RedirectAttributes redirectAttributes,ServletRequest request)
	{
		System.out.println("create");
		System.out.println("courseTaxonomyId" + request.getParameter("courseTaxonomyId"));

		Long courseTaxonomyId = Long.valueOf(request.getParameter("courseTaxonomyId"));
		
		User user = new User();
		user.setId(getCurrentUserId());
		newCourse.setUser(user);
		
		CourseTaxonomy courseTaxonomy = new CourseTaxonomy();
		courseTaxonomy.setId(courseTaxonomyId);
		
		newCourse.setCourseTaxonomy(courseTaxonomy);
		

		courseService.saveCourse(newCourse);
		
		redirectAttributes.addFlashAttribute("message", "创建教学成功");
		
		return "redirect:/ + hobby +“/course/”";
	}
	
	
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(
			@PathVariable String hobby,
			@PathVariable("id") Long id, Model model)
	{
		model.addAttribute("course", courseService.getCourse(id));
		model.addAttribute("action", "update");
		
		model.addAttribute("hobby", hobby);
		return "/" + hobby + "/course/courseForm";
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(
			@PathVariable String hobby,
			@Valid @ModelAttribute("course") Course newCourse, RedirectAttributes redirectAttributes)
	{
		//当前暂未更新的数据
		Course curCourse = (Course) courseService.getCourse(newCourse.getId());
		
		
		//将现有内容拷贝为历史版本
		Course courseRevision = new Course();
		courseRevision.setName(curCourse.getName()+"-revision");
		courseRevision.setContent(curCourse.getContent());
		courseRevision.setType("revision");
		courseRevision.setUser(curCourse.getUser());
		courseRevision.setCourseTaxonomy(curCourse.getCourseTaxonomy());
		courseRevision.setPid(curCourse.getId());
		courseService.saveCourse(courseRevision);
		
		//将新内容更新至原始数据库行
		curCourse.setName(newCourse.getName());
		curCourse.setContent(newCourse.getContent());
		courseService.saveCourse((Course)curCourse);
		
		//TODO pid
		return "redirect:/" + hobby +"/course/";
	}
	
	@RequestMapping(value = "revision/{id}", method = RequestMethod.GET)
	public String revision(
			@PathVariable String hobby,
			@PathVariable("id") Long id, Model model)
	{
		Course version = courseService.getCourse(id);
		List<Course> revision = courseService.getRevisions(id);
		
		model.addAttribute("version", version);
		model.addAttribute("preversion", revision.get(revision.size()-1));
		
		model.addAttribute("hobby", hobby);
		return "/" + hobby +  "/course/courseRevision";
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
