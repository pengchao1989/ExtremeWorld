package com.yumfee.extremeworld.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Reply;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.ReplyDTO;
import com.yumfee.extremeworld.service.ReplyService;

@RestController
@RequestMapping(value = "/api/v1/reply")
public class ReplyRestController
{
	private static Logger logger = LoggerFactory.getLogger(ReplyRestController.class);
	
	private static final String PAGE_SIZE = "5";
	
	@Autowired
	private ReplyService replyService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(
			@RequestParam(value = "topicId") Long topicId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType)
	{
		
		Page<Reply> replyPage = replyService.getAll(topicId, pageNumber, pageSize);
		
		MyPage<ReplyDTO, Reply> replyPageNew = new MyPage<ReplyDTO, Reply>(ReplyDTO.class, replyPage);
		
		return MyResponse.ok(replyPageNew);
	}
	
}
