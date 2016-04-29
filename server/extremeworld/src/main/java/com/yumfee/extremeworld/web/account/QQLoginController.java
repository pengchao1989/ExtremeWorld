package com.yumfee.extremeworld.web.account;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import com.yumfee.extremeworld.entity.Country;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.service.ReferenceAvatarService;
import com.yumfee.extremeworld.service.account.AccountService;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;

@Controller
@RequestMapping(value = "/qqlogin")
public class QQLoginController
{
	@Autowired
	private AccountService accountService;
	
	@Autowired 
	private ReferenceAvatarService referenceAvatarService;
	
	private com.yumfee.extremeworld.entity.User myUser = null;
	
	private boolean isNewUser = false;
	
	@RequestMapping(method = RequestMethod.GET)
	public void qqLogin(@CookieValue(value = "inviteid", defaultValue = "1") String inviteidCookie,
			ServletRequest request ,HttpServletResponse response) throws IOException {
		
		System.out.println("/qqlogin");
		System.out.println("inviteidCookie=" + inviteidCookie);

		
		response.setContentType("text/html;charset=utf-8");
        try 
        {
            response.sendRedirect(new Oauth().getAuthorizeURL(request));
        } catch (QQConnectException e) {
            e.printStackTrace();
        }
        
		//model.addAttribute("accessToken", accessToken);
		
		///return "account/qqLogin";
	}
	
	
	
	@RequestMapping(value = "redirect", method = RequestMethod.GET)
	public String qqLoginRedirect(@CookieValue(value = "inviteid", defaultValue = "1") String inviteidCookie, 
			@CookieValue(value = "inviteHobby", defaultValue = "skateboard") String inviteHobbyCookie,
			HttpServletRequest  request ,HttpServletResponse response) throws IOException
	{
		System.out.println("/redirect");
		return qqLoginAfter(inviteidCookie, inviteHobbyCookie, request,response);
	}
	
	@RequestMapping(value = "redirect",method = RequestMethod.POST)
	public String qqLoginAfter(String inviteid, String inviteHobby,
			HttpServletRequest  request ,HttpServletResponse response) throws IOException
	{
		System.out.println("/qqLoginAfter");
	
		System.out.println("inviteidCookie=" + inviteid);
		
		response.setContentType("text/html; charset=utf-8");

        PrintWriter out = response.getWriter();

        try {
            AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);

            String accessToken = null,openID = null;
            long tokenExpireIn = 0L;


            if (accessTokenObj.getAccessToken().equals("")) 
            {
//                我们的网站被CSRF攻击了或者用户取消了授权
//                做一些数据统计工作
                System.out.print("没有获取到响应参数");
            } 
            else 
            {
                accessToken = accessTokenObj.getAccessToken();
                tokenExpireIn = accessTokenObj.getExpireIn();

                request.getSession().setAttribute("demo_access_token", accessToken);
                request.getSession().setAttribute("demo_token_expirein", String.valueOf(tokenExpireIn));

                // 利用获取到的accessToken 去获取当前用的openid -------- start
                OpenID openIDObj =  new OpenID(accessToken);
                openID = openIDObj.getUserOpenID();

                
                //检验是否已经注册
                myUser = accountService.findUserByQqOpenId(openID);
                if(myUser == null)
                {
                	com.qq.connect.api.qzone.UserInfo qzoneUserInfo = new com.qq.connect.api.qzone.UserInfo(accessToken, openID);
                    UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                    
                    out.println("<br/>");
                    if (userInfoBean.getRet() == 0) 
                    {
                        out.println(userInfoBean.getNickname() + "<br/>");
                        out.println(userInfoBean.getGender() + "<br/>");
                        out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL30() + "/><br/>");
                        out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL50() + "/><br/>");
                        out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL100() + "/><br/>");
                    } 
                    else 
                    {
                        out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + userInfoBean.getMsg());
                    }
                    
                    
                	myUser = new com.yumfee.extremeworld.entity.User();
                	myUser.setQqOpenId(openID);
                	myUser.setLoginName(openID);
                	myUser.setName(userInfoBean.getNickname()+"[NotActivated]");
                	myUser.setPlainPassword(openID);
                	myUser.setAvatar(referenceAvatarService.getRandom().getUrl());

                	
                	Country country = new Country();
                	country.setId("CN");
                	myUser.setCountry(country);
                	
                	com.yumfee.extremeworld.entity.User inviter = new com.yumfee.extremeworld.entity.User();
                	inviter.setId(Long.parseLong(inviteid));
                	myUser.setInviter(inviter);
                	
                	accountService.registerUser(myUser);
                	
                	
                	//第一次qq登录进入欢迎页
                	isNewUser = true;
                	
                }
                
                //下面几行语句执行后进入ShiroDbRealm doGetAuthenticationInfo函数，校验合法性
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(openID,openID);
                token.setRememberMe(true);
                subject.login(token);
                
                out.println("欢迎你，代号为 " + openID + " 的用户!");
                request.getSession().setAttribute("demo_openid", openID);
                request.getSession().setAttribute("name", myUser.getName());
                out.println("<a href=" + "/shuoshuoDemo.html" +  " target=\"_blank\">去看看发表说说的demo吧</a>");
                // 利用获取到的accessToken 去获取当前用户的openid --------- end

            }
        }
        catch (QQConnectException e) 
        {
        }
        
        if(isNewUser)
        {
        	return "redirect:/" + inviteHobby + "/invite2/download";
        }
        else
        {
        	return "redirect:/all";
        }
        
	}
	
	@RequestMapping(value = "welcome", method = RequestMethod.GET)
	public String updateProfileForm(Model model)
	{
		Long currentUserId = getCurrentUserId();
		User currUserInfo = accountService.getUser(currentUserId);
		
		model.addAttribute("user", currUserInfo);
		model.addAttribute("action", "updateProfile");
		return "/account/welcome";
	}
	
	@RequestMapping(value = "updateProfile", method = RequestMethod.POST)
	public String updateProfile(@Valid @ModelAttribute("user") User user, RedirectAttributes redirectAttributes)
	{
		System.out.println("/updateProfile");
		
		String birth = user.getBirth();
		if(birth.length() == 4 )
		{
			birth += "-01-01";
		}
		user.setBirth(birth);
		
		accountService.updateUser(user);
		updateCurrentUserName(user.getName());
		return "redirect:/";
	}
	
	@ModelAttribute
	public void getUser(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("user", accountService.getUser(id));
		}
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
	
	/**
	 * 更新Shiro中当前用户的用户名.
	 */
	private void updateCurrentUserName(String userName) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		user.name = userName;
	}
}
