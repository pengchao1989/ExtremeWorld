package com.yumfee.extremeworld.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Reply;
import com.yumfee.extremeworld.service.ReplyService;
import com.yumfee.extrmeworld.rest.dto.ReplyDTO;

@RestController
@RequestMapping(value = "/api/v1/reply")
public class ReplyRestController
{
	private static Logger logger = LoggerFactory.getLogger(ReplyRestController.class);
	
	@Autowired
	private ReplyService replyService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public List<ReplyDTO> list()
	{
		List<Reply> replyEntityList = replyService.getAll();
		
		List<ReplyDTO> replyDTOs = BeanMapper.mapList(replyEntityList, ReplyDTO.class);
		return replyDTOs;
	}
	
}
