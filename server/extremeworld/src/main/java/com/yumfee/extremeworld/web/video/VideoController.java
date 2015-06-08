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

import com.yumfee.extremeworld.config.HobbyPathConfig;
import com.yumfee.extremeworld.entity.Reply;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.entity.Video;
import com.yumfee.extremeworld.service.ReplyService;
import com.yumfee.extremeworld.service.VideoService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@Controller
@RequestMapping(value = "{hobby}/video")
public class VideoController
{
	
	private static final String PAGE_SIZE = "24";

	@Autowired
	private VideoService videoService;
	
	@Autowired
	private ReplyService replyService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@PathVariable String hobby,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
	{
		Page<Video> videos = null;
		Long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		
		if(hobbyId == 0L)
		{
			videos = videoService.getAllVideo(pageNumber, pageSize, sortType);
		}
		else
		{
			videos = videoService.getByHobby(hobbyId, pageNumber, pageSize, sortType);
		}
		
		
		
		model.addAttribute("videos", videos);
		model.addAttribute("hobby", hobby);
		
		return "/video/videoList";
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
				Video video = videoService.getVideo(id);
				Page<Reply> replys = replyService.getAll(id,pageNumber, pageSize);
		
				Long userId = getCurrentUserId();
				
				model.addAttribute("topic", video);
				model.addAttribute("replys", replys);
				
				model.addAttribute("userId", userId);
				model.addAttribute("type", "video");
				
				model.addAttribute("hobby", hobby);
				
				return "discuss/discussDetail";
			}
	
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(
			@PathVariable String hobby,
			Model model)
	{
		model.addAttribute("video", new Video());
		model.addAttribute("action", "create");
		
		model.addAttribute("hobby", hobby);
		
		return "/video/videoForm";
	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(
			@PathVariable String hobby,
			@Valid Video newVideo, 
			RedirectAttributes redirectAttributes)
	{
		User user = new User();
		user.setId(getCurrentUserId());
		newVideo.setUser(user);
		newVideo.setImageCount(0);
		newVideo.setReplyCount(0);
		newVideo.setStatus(1);
		newVideo.setExcerpt(newVideo.getContent());
		
		videoService.saveVideo(newVideo);
		
		redirectAttributes.addFlashAttribute("message", "发布视频成功");
		return "redirect:/" + hobby +"/video/";
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user != null)
		{
			return user.id;
		}
		return null;
		
	}
}
