package com.yumfee.extremeworld.web.biz.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yumfee.extremeworld.entity.Hobby;
import com.yumfee.extremeworld.entity.biz.Category;
import com.yumfee.extremeworld.entity.biz.Goods;
import com.yumfee.extremeworld.entity.biz.Shop;
import com.yumfee.extremeworld.service.account.ShiroDbRealm.ShiroUser;
import com.yumfee.extremeworld.service.biz.CategoryService;
import com.yumfee.extremeworld.service.biz.GoodsService;
import com.yumfee.extremeworld.service.biz.ShopService;

@Controller
@RequestMapping(value = "/shop/goods")
public class GoodsAdminController {

	private static Logger logger = LoggerFactory.getLogger(GoodsAdminController.class);
			
	private static final String PAGE_SIZE = "10";
	
	@Autowired 
	private ShopService shopService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private GoodsService goodsService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, 
			ServletRequest request){
		
		//Long userId = getCurrentUserId();
		Shop shop = shopService.findByUserId(getCurrentUserId());
		if(shop == null){
			return "error/500.jsp";
		}
		
		Page<Goods> goodsPage = goodsService.getByShoopId(shop.getId(), pageNumber, pageSize, "id");
		
		model.addAttribute("goodsPage", goodsPage);
		return "/biz/shop/myGoodsList"; 
	}
	
	@RequestMapping(value="create",method = RequestMethod.GET)
	public String createForm(Model model, ServletRequest request){
		
		Shop shop = shopService.findByUserId(getCurrentUserId());
		if(shop == null){
			return "error/500.jsp";
		}
		
/*		List<List<Category>> hobbyCategoryList = new ArrayList<List<Category>>();
		for(Hobby hobby:shop.getHobbys()){
			List<Category> categoryList = categoryService.getAllByHobbyId(hobby.getId());
			
			hobbyCategoryList.add(categoryList);
		}*/
		
		
		List<Hobby> hobbys = shop.getHobbys();
		if(hobbys.size() > 0);
		List<Category> categoryList = categoryService.getAllByHobbyId(hobbys.get(0).getId());
		
		
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("action", "create");
		model.addAttribute("goods", new Goods());
		
		return "/biz/shop/goodsForm";
	}
	
	@RequestMapping(value="create",method = RequestMethod.POST)
	public String create(
			@Valid Goods goods,
			ServletRequest request,
			RedirectAttributes redirectAttributes){
		
		String categoryId = request.getParameter("categoryId");
		List<Category> categoryList = new ArrayList<Category>();
		Category category = new Category();
		category.setId(Long.parseLong(categoryId));
		categoryList.add(category);
		
		goods.setCategorys(categoryList);
		
		Shop shop = shopService.findByUserId(getCurrentUserId());
		goods.setShop(shop);
		
		goods.setType("new");
		
		goodsService.save(goods);

		return "redirect:" + "/shop/goods/";
		
	}
	
	
	
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
