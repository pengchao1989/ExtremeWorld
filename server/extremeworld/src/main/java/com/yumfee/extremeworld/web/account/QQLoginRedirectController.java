package com.yumfee.extremeworld.web.account;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import com.yumfee.extremeworld.service.account.AccountService;

@Controller
@RequestMapping(value = "/qqlogin_redirect")
public class QQLoginRedirectController
{
	
	@Autowired
	private AccountService accountService;
	
	private com.yumfee.extremeworld.entity.UserInfo myUser = null;
	
	@RequestMapping(method = RequestMethod.GET)
	public String get(HttpServletRequest  request ,HttpServletResponse response) throws IOException
	{
		return qqLoginAfter(request, response);
	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	public String qqLoginAfter(HttpServletRequest  request ,HttpServletResponse response) throws IOException
	{
		System.out.println("/qqLoginAfter");
		
		
		
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
                	UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
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
                    
                    
                	myUser = new com.yumfee.extremeworld.entity.UserInfo();
                	myUser.setQqOpenId(openID);
                	myUser.setLoginName(openID);
                	myUser.setName(userInfoBean.getNickname());
                	myUser.setPlainPassword(openID);
                	
                	accountService.registerUser(myUser);
                	
                	
                	//第一次qq登录进入欢迎页
                	
                	
                }
                
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(openID,openID);
                subject.login(token);
                
                out.println("欢迎你，代号为 " + openID + " 的用户!");
                request.getSession().setAttribute("demo_openid", openID);
                out.println("<a href=" + "/shuoshuoDemo.html" +  " target=\"_blank\">去看看发表说说的demo吧</a>");
                // 利用获取到的accessToken 去获取当前用户的openid --------- end

            }
        }
        catch (QQConnectException e) 
        {
        }
    
        return "redirect:/topic";
	}
}
