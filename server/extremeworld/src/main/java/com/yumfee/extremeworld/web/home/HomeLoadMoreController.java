package com.yumfee.extremeworld.web.home;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yumfee.extremeworld.entity.Activity;
import com.yumfee.extremeworld.entity.Discussion;
import com.yumfee.extremeworld.entity.Mood;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.Video;
import com.yumfee.extremeworld.service.ReplyService;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.UserService;

@Controller
@RequestMapping(value = "{hobby}/loadmore")
public class HomeLoadMoreController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private ReplyService replyService;
	
	private static final String PAGE_SIZE = "10";
	
	@RequestMapping(method = RequestMethod.GET)
	public String list_fragment(
			@PathVariable String hobby,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
	{
		Page<Topic> topics = topicService.getAllTopic(pageNumber, pageSize, sortType);
		
		//Page<Topic> topics = topicService.getTopicByfollowings(2L,pageNumber, pageSize, sortType);
		
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Topic topic : topics)
		{
			if(topic instanceof Discussion)
			{
				topic.setTitle("[讨论]" + topic.getTitle());
				model.addAttribute("type", "discuss");
			}
			else if(topic instanceof Mood)
			{
				topic.setTitle("[心情]" + topic.getTitle());
				model.addAttribute("type", "mood");
			}
			else if(topic instanceof Video)
			{
				topic.setTitle("[视频]" + topic.getTitle());
				model.addAttribute("type", "video");
			}
			else if(topic instanceof Activity)
			{
				topic.setTitle("[活动]" + topic.getTitle());
				model.addAttribute("type", "activity");
			}
		}
		
		model.addAttribute("hobby", hobby);
		model.addAttribute("topics", topics);
		
		return "/home/moreFragment";
	}
}
