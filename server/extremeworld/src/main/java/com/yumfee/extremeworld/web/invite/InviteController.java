package com.yumfee.extremeworld.web.invite;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yumfee.extremeworld.entity.Invite;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.service.InviteService;
import com.yumfee.extremeworld.service.UserService;

@Controller
@RequestMapping(value = "/{hobby}/invite")
public class InviteController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private InviteService inviteService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String invite(
			@PathVariable String hobby,
			@RequestParam(value = "inviteid", defaultValue = "1") long inviteid,
			Model model, ServletRequest request){
		
		User user = userService.getUser(inviteid);
		if(user != null){
			model.addAttribute("inviter", user);
		}
		model.addAttribute("hobby",hobby);
		
		return "/invite/invite";
	}
	
	
	@RequestMapping( method = RequestMethod.POST)
	public String create(
			@PathVariable String hobby,
			ServletRequest request){
		
		Long inviterId = Long.valueOf(request.getParameter("inviterId"));
		String phone = request.getParameter("phone");
		
		User inviteUser = userService.getUser(inviterId);
		
		if(phone != null && inviteUser != null){
			Invite invite = new Invite();
			invite.setPhone(phone);
			
			
			invite.setInviteUser(inviteUser);
			inviteService.saveInvite(invite);
		}
		
		return "redirect:/" + hobby + "/invite/download";
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public String download(@PathVariable String hobby){
		return "/invite/download";
	}
	
}
