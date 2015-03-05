package com.yumfee.extremeworld.web.test;

import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yumfee.extremeworld.service.RedisTestService;

@Controller
@RequestMapping(value = "/redis")
public class RedisTestController 
{
	@RequestMapping(method = RequestMethod.GET)
	public String getValues(Model model, ServletRequest request)
	{
		RedisTestService redisTestService = new RedisTestService();
		
		List<String> values = redisTestService.getValues("abc", "bcd");
		
		model.addAttribute("values", values);
		
		return "test/redis_values";
	}
	
	@RequestMapping(value = "{key}", method = RequestMethod.GET)
	public String getValue(@PathVariable("key") String key,
			Model model, ServletRequest request)
	{
		RedisTestService redisTestService = new RedisTestService();
		
		Long value = redisTestService.incr(key);
		
		model.addAttribute("value",value); 
		return "test/redis";
	}
	
	
}
