package com.yumfee.extremeworld.web.topic;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import com.yumfee.extremeworld.entity.Video;
import com.yumfee.extremeworld.service.ReplyService;
import com.yumfee.extremeworld.service.TopicService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@Controller
@RequestMapping(value = "{hobby}/topic")
public class TopicController
{
	private static final String PAGE_SIZE = "10";
	
	@Autowired
	private TopicService topicService;
	
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
		//Page<Topic> topics = topicService.getAllTopic(pageNumber, pageSize, sortType);
		
		Page<Topic> topics = topicService.getTopicByfollowings(2L,pageNumber, pageSize, sortType);
		
		model.addAttribute("topics", topics);
		
		model.addAttribute("hobby",hobby);
		
		return "discuss/discussList";
	}
	
	@RequestMapping( method = RequestMethod.POST)
	public String create(
			@PathVariable String hobby,
			@Valid Topic newTopic, RedirectAttributes redirectAttributes)
	{
		User user = new User();
		user.setId(getCurrentUserId());
		newTopic.setUser(user);
		newTopic.setImageCount(0);
		newTopic.setReplyCount(0);
		newTopic.setStatus(1);
		
		topicService.saveHtmlTopic(newTopic);
		redirectAttributes.addFlashAttribute("message", "添加话题成功");
		return "redirect:/" + hobby + "/discuss/";
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
		
		//第一页显示主题内容
		if(pageNumber==1)
		{
			
		}
		Topic topic = topicService.getTopic(id);
		
		
		//在此处理content中图片地址，以更适合在web端显示
		String newContent = replaceImageUrl(topic.getContent());
		
		System.out.print(newContent);
		
		//topic.setContent(newContent);

		
		
		Page<Reply> replys = replyService.getAll(id,pageNumber, pageSize);
		
		if(topic instanceof Video)
		{
			model.addAttribute("type", "video");
			Long userId = getCurrentUserId();
			model.addAttribute("userId", userId);
		}
		else if(topic instanceof Topic)
		{
			model.addAttribute("type", "topic");
		}
			
		
		
		model.addAttribute("topic",topic);
		model.addAttribute("topicContent", newContent);
		model.addAttribute("replys", replys);
		
		model.addAttribute("hobby",hobby);
		
		return "discuss/discussDetail";
	}
	
	//回复
	@RequestMapping(value = "{id}", method = RequestMethod.POST)
	public String createReply(
			@PathVariable String hobby,
			@PathVariable("id") Long topicId, 
			@Valid Reply newReply, 
			RedirectAttributes redirectAttributes)
	{
		
		newReply.setId(null);
		
		User userInfo = new User();
		userInfo.setId(getCurrentUserId());
		Topic topic = new Topic();
		topic.setId(topicId);
		
		newReply.setUser(userInfo);
		newReply.setTopic(topic);
		
		replyService.saveReply(newReply);
		
		
		
		return "redirect:/" + hobby + "/topic/"+ topicId;
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
	
	
	private String replaceImageUrl(String content)
	{
		
		String regexPIC_URL="http://img\\.jixianxueyuan\\.com.*";
		
		Document contnetDocument = Jsoup.parseBodyFragment(content);
		
		Elements images = contnetDocument.getElementsByTag("img");
		for(Element img : images)
		{
			String src = img.attr("src");
			
			if(src.matches(regexPIC_URL) && (!src.contains("!webContentImg")))
			{
				img.attr("src", src + "!webContentImg");
			}
			
		}
		

		return contnetDocument.body().html();
	}
}
