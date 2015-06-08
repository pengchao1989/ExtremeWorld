package com.yumfee.extremeworld.web.other;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "{hobby}/todoList")
public class TodoListController
{
	@RequestMapping(method = RequestMethod.GET)
	public String get(
			@PathVariable String hobby,
			Model model, 
			ServletRequest request)
	{
		
		model.addAttribute("hobby",hobby);
		return "other/todoList";
	}
}
