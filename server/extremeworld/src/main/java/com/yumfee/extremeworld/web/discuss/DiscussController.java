package com.yumfee.extremeworld.web.discuss;

import java.util.List;

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

import com.yumfee.extremeworld.entity.Discussion;
import com.yumfee.extremeworld.entity.Reply;
import com.yumfee.extremeworld.entity.Taxonomy;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.entity.Video;
import com.yumfee.extremeworld.service.DiscussionService;
import com.yumfee.extremeworld.service.ReplyService;
import com.yumfee.extremeworld.service.TaxonomyService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@Controller
@RequestMapping(value = "/discuss")
public class DiscussController {

	private static final String PAGE_SIZE = "10";
	
	@Autowired
	private DiscussionService discussionService;
	
	@Autowired
	private TaxonomyService taxonomyService;
	
	@Autowired
	private ReplyService replyService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "taxonomy", defaultValue = "0") Long taxonomyId,
			@RequestParam(value = "hobby", defaultValue = "0") Long hobbyId,
			Model model, ServletRequest request)
	{
		
		Page<Discussion> topics = null;
		
		if(0 == hobbyId)
		{
			if(taxonomyId != 0)
			{
				topics	= discussionService.getByHobbyAndTaxonomy(hobbyId, taxonomyId, pageNumber, pageSize, sortType);
			}
			else
			{
				topics = discussionService.getByHobby(hobbyId, pageNumber, pageSize, sortType);
			}
		}
		else
		{
			if(taxonomyId != 0)
			{
				topics	= discussionService.getByHobbyAndTaxonomy(hobbyId, taxonomyId, pageNumber, pageSize, sortType);
			}
			else
			{
				topics = discussionService.getByHobby(hobbyId, pageNumber, pageSize, sortType);
			}
		}
		

		 
		List<Taxonomy> taxonomys = taxonomyService.getTaxonomyByHobby(hobbyId);
		
		model.addAttribute("currentTaxonomyId", taxonomyId);
		model.addAttribute("taxonomys", taxonomys);
		model.addAttribute("topics", topics);
		
		model.addAttribute("hobbyId",hobbyId);
		
		return "discuss/discussList";
	}
	
	@RequestMapping( method = RequestMethod.POST)
	public String create(@Valid Discussion newTopic, RedirectAttributes redirectAttributes)
	{
		User user = new User();
		user.setId(getCurrentUserId());
		newTopic.setUser(user);
		newTopic.setImageCount(0);
		newTopic.setReplyCount(0);
		newTopic.setStatus(1);
		
		discussionService.saveDiscussion(newTopic);
		redirectAttributes.addFlashAttribute("message", "添加话题成功");
		return "redirect:/discuss/";
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
		Topic topic = discussionService.getDiscussion(id);
		
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
		model.addAttribute("replys", replys);
		
		
		return "discuss/discussDetail";
	}
	
	//子回复
	@RequestMapping(value = "{id}", method = RequestMethod.POST)
	public String createReply(@PathVariable("id") Long topicId, @Valid Reply newReply, RedirectAttributes redirectAttributes)
	{
		
		newReply.setId(null);
		
		User userInfo = new User();
		userInfo.setId(getCurrentUserId());
		Topic topic = new Topic();
		topic.setId(topicId);
		
		newReply.setUser(userInfo);
		newReply.setTopic(topic);
		
		replyService.saveReply(newReply);
		
		
		
		return "redirect:/discuss/"+ topicId;
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
