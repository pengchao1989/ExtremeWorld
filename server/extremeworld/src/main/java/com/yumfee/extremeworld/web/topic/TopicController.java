package com.yumfee.extremeworld.web.topic;

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

import com.yumfee.extremeworld.entity.Reply;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.entity.UserInfo;
import com.yumfee.extremeworld.service.ReplyService;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@Controller
@RequestMapping(value = "/topic")
public class TopicController
{
	private static final String PAGE_SIZE = "10";
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private ReplyService replyService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
	{
		Page<Topic> topics = topicService.getAllTopic(pageNumber, pageSize, sortType);
		
		//Page<Topic> topics = topicService.getAllTopicByCourse(1L,pageNumber, pageSize, sortType);
		
		model.addAttribute("topics", topics);
		return "topic/topicList";
	}
	
	@RequestMapping( method = RequestMethod.POST)
	public String create(@Valid Topic newTopic, RedirectAttributes redirectAttributes)
	{
		User user = new User(getCurrentUserId());
		newTopic.setUser(user);
		newTopic.setImageCount(0);
		newTopic.setReplyCount(0);
		newTopic.setStatus(1);
		
		topicService.saveTopic(newTopic);
		redirectAttributes.addFlashAttribute("message", "添加话题成功");
		return "redirect:/topic/";
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") Long id, 
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
	{
		
		//第一页显示主题内容
		if(pageNumber==1)
		{
			
		}
		Topic topic = topicService.getTopic(id);
		
		Page<Reply> replys = replyService.getAll(id,pageNumber, pageSize);
		
		model.addAttribute("topic",topic);
		model.addAttribute("replys", replys);
		return "topic/topicDetail";
	}
	
	//子回复
	@RequestMapping(value = "{id}", method = RequestMethod.POST)
	public String createReply(@PathVariable("id") Long topicId, @Valid Reply newReply, RedirectAttributes redirectAttributes)
	{
		
		newReply.setId(null);
		
		UserInfo userInfo = new UserInfo();
		userInfo.setId(getCurrentUserId());
		Topic topic = new Topic();
		topic.setId(topicId);
		
		newReply.setUserInfo(userInfo);
		newReply.setTopic(topic);
		
		replyService.saveReply(newReply);
		
		
		
		return "redirect:/topic/"+ topicId;
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
