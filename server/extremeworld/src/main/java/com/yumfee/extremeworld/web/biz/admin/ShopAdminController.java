package com.yumfee.extremeworld.web.biz.admin;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yumfee.extremeworld.entity.biz.Shop;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;
import com.yumfee.extremeworld.service.biz.ShopService;

@Controller
@RequestMapping(value = "/shop")
public class ShopAdminController {

	@Autowired 
	private ShopService shopService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String my(Model model, 
			ServletRequest request){
		
		//Long userId = getCurrentUserId();
		//Shop shop = shopService.findByUserId(userId);
		Shop shop = shopService.findById(1L);
		
		model.addAttribute("shop", shop);
		
		return "/biz/shop/myShop";
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
