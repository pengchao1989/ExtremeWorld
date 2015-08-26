package com.yumfee.extremeworld.rest;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.SubReply;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.SubReplyDTO;
import com.yumfee.extremeworld.service.SubReplyService;

@RestController
@RequestMapping(value = "/api/v1/sub_reply")
public class SubReplyRestController {
	private static Logger logger = LoggerFactory.getLogger(SubReplyRestController.class);
	
	@Autowired
	SubReplyService subReplyService;
	
	@Autowired
	private Validator validator;
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse create(@RequestBody SubReply subReply){
		
		BeanValidators.validateWithException(validator,subReply);
		
		subReplyService.saveSubReply(subReply);
		SubReplyDTO subReplyDTO = BeanMapper.map(subReply, SubReplyDTO.class);
		
		return MyResponse.ok(subReplyDTO);
		
	}
	
}
