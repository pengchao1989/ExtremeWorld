package com.yumfee.extremeworld.web.video;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yumfee.extremeworld.entity.Task;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.entity.UserInfo;
import com.yumfee.extremeworld.entity.Video;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.VideoService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@Controller
@RequestMapping(value = "/video")
public class VideoController
{
	
	private static final String PAGE_SIZE = "32";

	@Autowired
	private VideoService videoService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
	{
		Page<Video> videos = videoService.getAllVideo(pageNumber, pageSize, sortType);
		
		model.addAttribute("videos", videos);
		
		return "/video/videoList";
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") Long id, 
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
			{
				Video video = videoService.getVideo(id);
		
				Long userId = getCurrentUserId();
				
				model.addAttribute("video", video);
				model.addAttribute("userId", userId);
				
				return "/video/videoDetail";
			}
	
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model)
	{
		model.addAttribute("video", new Video());
		model.addAttribute("action", "create");
		
		
		return "/video/videoForm";
	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Video newVideo, RedirectAttributes redirectAttributes)
	{
		UserInfo user = new UserInfo();
		user.setId(getCurrentUserId());
		newVideo.setUserInfo(user);
		newVideo.setImageCount(0);
		newVideo.setReplyCount(0);
		newVideo.setStatus(1);
		newVideo.setExcerpt(newVideo.getContent());
		
		videoService.saveVideo(newVideo);
		
		redirectAttributes.addFlashAttribute("message", "发布视频成功");
		return "redirect:/task/";
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
