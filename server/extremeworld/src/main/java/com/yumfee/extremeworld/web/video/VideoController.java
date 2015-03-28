package com.yumfee.extremeworld.web.video;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.Video;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.VideoService;

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
		
				model.addAttribute("video", video);
				
				return "/video/videoDetail";
			}
}
