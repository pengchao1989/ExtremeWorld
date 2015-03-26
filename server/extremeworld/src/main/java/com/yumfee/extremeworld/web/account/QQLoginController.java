package com.yumfee.extremeworld.web.account;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.qq.connect.QQConnectException;
import com.qq.connect.oauth.Oauth;

@Controller
@RequestMapping(value = "/qqlogin")
public class QQLoginController
{
	@RequestMapping(method = RequestMethod.GET)
	public void qqLogin(ServletRequest request ,HttpServletResponse response) throws IOException {
		
		System.out.println("/qqlogin");
		
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
}
