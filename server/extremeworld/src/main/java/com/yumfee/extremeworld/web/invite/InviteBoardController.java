package com.yumfee.extremeworld.web.invite;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.service.UserService;

@Controller
@RequestMapping(value = "/other/invite_board")
public class InviteBoardController {
	
	private static final int PAGE_SIZE = 30;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String invite(
			@RequestParam(value = "userid", defaultValue = "1") long userid,
			@RequestParam(value = "hobby", defaultValue = "skateboard") String hobby,
			Model model, HttpServletRequest request,HttpServletResponse response){
		

		User user = userService.getUser(userid);
		Page<User> myInviteUserPage = userService.findByInviterId(userid, 1, PAGE_SIZE);
		List<User> inviteUserList = myInviteUserPage.getContent();
		if(user != null){
			model.addAttribute("user", user);
		}
		model.addAttribute("hobby",hobby);
		model.addAttribute("inviteUserList", inviteUserList);
		
		return "/invite/invite_board";
	}
}
