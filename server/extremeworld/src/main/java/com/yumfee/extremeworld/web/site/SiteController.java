package com.yumfee.extremeworld.web.site;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yumfee.extremeworld.entity.Site;
import com.yumfee.extremeworld.service.SiteService;

@Controller
@RequestMapping(value = "/site")
public class SiteController
{
	private static final String PAGE_SIZE = "16";
	
	@Autowired
	private SiteService siteService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request)
			{
				Page<Site> sites = siteService.getAllSite(pageNumber, pageSize);
				
				model.addAttribute("sites", sites);
				System.out.println(request.getServerName());
				model.addAttribute("name", request.getServerName());
				
				return "/site/siteList";
			}
	
}
