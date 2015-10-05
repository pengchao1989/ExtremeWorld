package com.yumfee.extremeworld.web.invite;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.service.InviteService;
import com.yumfee.extremeworld.service.UserService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@Controller
@RequestMapping(value = "/{hobby}/invite2")
public class InviteQQController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private InviteService inviteService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String invite(
			@PathVariable String hobby,
			@RequestParam(value = "inviteid", defaultValue = "1") long inviteid,
			Model model, HttpServletRequest request,HttpServletResponse response){
		
		Cookie cookie = new Cookie("inviteid", String.valueOf(inviteid));
		Cookie hobbyCookie = new Cookie("inviteHobby", hobby);
		cookie.setMaxAge(10000);
		hobbyCookie.setMaxAge(10000);
		response.addCookie(cookie);
		response.addCookie(hobbyCookie);
		

		User user = userService.getUser(inviteid);
		if(user != null){
			model.addAttribute("inviter", user);
		}
		model.addAttribute("hobby",hobby);
		
		return "/invite/invite2";
	}
	
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public String download(@PathVariable String hobby,
			@CookieValue(value = "inviteid", defaultValue = "1") String inviteidCookie,
			@CookieValue(value = "inviteHobby", defaultValue = "skateboard") String inviteHobby)
	{
		System.out.println("download,inviteidCookie=" + inviteidCookie);
		System.out.println("download,inviteHobby=" + inviteHobby);
		
		Long inviterId = Long.valueOf(inviteidCookie);
		User inviteUser = userService.getUser(inviterId);
		
		Long currentUserId = getCurrentUserId();
		User currUserInfo = userService.getUser(currentUserId);
		
		currUserInfo.setInviter(inviteUser);
		userService.saveUser(currUserInfo);
		return "/invite/download";
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
