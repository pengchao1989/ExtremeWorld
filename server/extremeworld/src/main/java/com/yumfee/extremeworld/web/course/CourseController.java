package com.yumfee.extremeworld.web.course;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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

import com.yumfee.extremeworld.config.HobbyPathConfig;
import com.yumfee.extremeworld.config.TopicStatus;
import com.yumfee.extremeworld.config.TopicType;
import com.yumfee.extremeworld.entity.Course;
import com.yumfee.extremeworld.entity.CourseTaxonomy;
import com.yumfee.extremeworld.entity.Hobby;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.entity.Video;
import com.yumfee.extremeworld.entity.VideoDetail;
import com.yumfee.extremeworld.service.CourseService;
import com.yumfee.extremeworld.service.CourseTaxonomyService;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.VideoService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@Controller
@RequestMapping(value = "{hobby}/course")
public class CourseController
{
	private static final String PAGE_SIZE = "16";
	
	@Autowired
	private CourseTaxonomyService courseTaxonomyService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired 
	private TopicService topicService;
	
	@Autowired
	private VideoService videoService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@PathVariable String hobby,
			Model model, 
			ServletRequest request)
	{
		Long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		
		List<CourseTaxonomy> courseTaxonomyList = courseTaxonomyService.findByHobby(hobbyId);
		
		model.addAttribute("courseTaxonomyList", courseTaxonomyList);
		
		model.addAttribute("hobby",hobby);
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
		
		Page<Topic> questions = topicService.getAllTopicByCourseAndMagicType(id, TopicType.magicQuestion, pageNumber, pageSize, sortType);
		Page<Topic> explains = topicService.getAllTopicByCourseAndMagicType(id, TopicType.magicExplain, pageNumber, pageSize, sortType);
		Page<Topic> sbs = topicService.getAllTopicByCourseAndMagicType(id, TopicType.magicSB, pageNumber, pageSize, sortType);
		
		model.addAttribute("course", course);
		model.addAttribute("sbs", sbs);
		model.addAttribute("questions", questions);
		model.addAttribute("explains", explains);
		
		model.addAttribute("hobby", hobby);
		return "/course/courseDetail";
	}
	
	@RequestMapping(value = "loadmore/{id}", method = RequestMethod.GET)
	public String loadMore(@PathVariable("id") Long id,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "agreeCount") String sortType,
			@RequestParam(value = "magicType", defaultValue = "explain") String magicType,
			Model model, ServletRequest request)
	{
		Page<Topic> questions = topicService.getAllTopicByCourseAndMagicType(id, magicType, pageNumber, pageSize, sortType);
		
		model.addAttribute("topics", questions);
		
		return "/course/topicFragment";
	}
	
	@RequestMapping(value = "loadQuestion/{id}", method = RequestMethod.GET)
	public String loadMoreOfQuestion(@PathVariable("id") Long id,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
	{
		Page<Topic> questions = topicService.getAllTopicByCourseAndMagicType(id, TopicType.magicQuestion, pageNumber, pageSize, sortType);
		
		model.addAttribute("topics", questions);
		
		return "/course/topicFragment";
	}
	
	@RequestMapping(value = "loadExplain/{id}", method = RequestMethod.GET)
	public String loadMoreOfCourse(@PathVariable("id") Long id,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
	{
		Page<Topic> explains = topicService.getAllTopicByCourseAndMagicType(id, TopicType.magicExplain, pageNumber, pageSize, sortType);
		
		model.addAttribute("topics", explains);
		
		return "/course/topicFragment";
	}
	
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(
			@PathVariable String hobby,
			Model model)
	{
		Long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		
		List<CourseTaxonomy> courseTaxonomyList = courseTaxonomyService.findByHobby(hobbyId);
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
		System.out.println("courseCatalogueId" + request.getParameter("courseCatalogueId"));

		Long courseCatalogueId = Long.valueOf(request.getParameter("courseCatalogueId"));
		
		User user = new User();
		user.setId(getCurrentUserId());
		newCourse.setUser(user);
		newCourse.setPid(0L);
		newCourse.setType("course");
		
		CourseTaxonomy courseTaxonomy = new CourseTaxonomy();
		courseTaxonomy.setId(courseCatalogueId);
		
		newCourse.setCourseTaxonomy(courseTaxonomy);
		

		courseService.saveCourse(newCourse);
		
		redirectAttributes.addFlashAttribute("message", "创建教学成功");
		
		return "redirect:/" + hobby +"/course";
	}
	
	
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(
			@PathVariable String hobby,
			@PathVariable("id") Long id, Model model)
	{
		model.addAttribute("course", courseService.getCourse(id));
		model.addAttribute("action", "update");
		
		model.addAttribute("hobby", hobby);
		return  "/course/courseForm";
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
		courseRevision.setName(curCourse.getName()+"-revision-");
		courseRevision.setContent(curCourse.getContent());
		courseRevision.setType("revision");
		courseRevision.setUser(curCourse.getUser());
		courseRevision.setCourseTaxonomy(curCourse.getCourseTaxonomy());
		courseRevision.setPid(curCourse.getId());
		courseService.saveCourse(courseRevision);
		
		//再将历史版本取出来，更新名字，fuck
		courseRevision.setName(courseRevision.getName() + courseRevision.getId());
		courseService.saveCourse(courseRevision);
		
		
		//将新内容更新至原始数据库行
		curCourse.setName(newCourse.getName());
		curCourse.setContent(newCourse.getContent());
		curCourse.setSortWeight(newCourse.getSortWeight());
		curCourse.setModifyTime(new Date());
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
		if(revision.size() > 1){
			model.addAttribute("preversion", revision.get(revision.size() - 1));
		}else{
			model.addAttribute("preversion", version);
		}
		
		
		model.addAttribute("hobby", hobby);
		return "/course/courseRevision";
	}
	
	@RequestMapping(value = "addExplan/{id}", method = RequestMethod.GET)
	public String addExplanForm(
			@PathVariable String hobby,
			@PathVariable("id") Long id,
			Model model)
	{
		model.addAttribute("video", new Video());
		model.addAttribute("action", "addExplain");
		model.addAttribute("hobby", hobby);
		model.addAttribute("course", courseService.getCourse(id));
		
		return "/course/addExplainForm";
	}
	
	@RequestMapping(value = "addExplain/{courseId}", method = RequestMethod.POST)
	public String addExplan(
			@PathVariable String hobby,
			@PathVariable("courseId") Long courseId,
			@Valid Topic newVideo, 
			ServletRequest request,
			RedirectAttributes redirectAttributes)
	{
		User user = new User();
		user.setId(getCurrentUserId());
		newVideo.setUser(user);
		newVideo.setReplyCount(0);
		newVideo.setStatus(TopicStatus.PUBLIC);
		newVideo.setExcerpt(newVideo.getContent());
		newVideo.setType(TopicType.COURSE);
		newVideo.setMagicType(TopicType.magicExplain);

		//videoSource在属性的属性中，单独从form参数中提取
		String videoSource = request.getParameter("videoSource");
		String frontSource = request.getParameter("frontSource");
		System.out.println(videoSource);
		System.out.println(frontSource);
		
		if(StringUtils.isNoneBlank(videoSource)){
			VideoDetail videoDetail = new VideoDetail();
			videoDetail.setVideoSource(videoSource);
			videoDetail.setThumbnail(frontSource);
			newVideo.setVideoDetail(videoDetail);
		}


		//hobby
		List<Hobby> hobbyList= new ArrayList<Hobby>();
		Long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		Hobby hobbyBean = new Hobby();
		hobbyBean.setId(hobbyId);
		hobbyList.add(hobbyBean);
		
		newVideo.setHobbys(hobbyList);
		
		//course info
		Course course = new Course();
		course.setId(courseId);
		newVideo.setCourse(course);
		
		topicService.saveTopic(newVideo);
		
		redirectAttributes.addFlashAttribute("message", "发布教程成功");
		return "redirect:/" + hobby +"/course/" + courseId;
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
