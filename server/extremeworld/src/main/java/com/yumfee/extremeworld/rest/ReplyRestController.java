package com.yumfee.extremeworld.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.service.ReplyService;

@RestController
@RequestMapping(value = "/api/v1/reply")
public class ReplyRestController
{
	private static Logger logger = LoggerFactory.getLogger(ReplyRestController.class);
	
	@Autowired
	private ReplyService replyService;
	
	
	
	
	
}
