package com.yumfee.extremeworld.rest;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.Reply;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.ReplyDTO;
import com.yumfee.extremeworld.service.ReplyService;

@RestController
@RequestMapping(value = "/api/secure/v1/reply")
public class ReplyRestController
{
	private static Logger logger = LoggerFactory.getLogger(ReplyRestController.class);
	
	private static final String PAGE_SIZE = "5";
	
	@Autowired
	private ReplyService replyService;
	
	@Autowired
	private Validator validator;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(
			@RequestParam(value = "topicId") Long topicId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		Page<Reply> replyPage = replyService.getAll(topicId, pageNumber, pageSize);	
		MyPage<ReplyDTO, Reply> myReplyPage = new MyPage<ReplyDTO, Reply>(ReplyDTO.class, replyPage);
		return MyResponse.ok(myReplyPage,true);
	}
	
	@RequestMapping(value = "/hot", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getHot(@RequestParam(value = "topicId") Long topicId){
		Page<Reply> replyPage = replyService.getHot(topicId);
		MyPage<ReplyDTO, Reply> myReplyPage = new MyPage<ReplyDTO, Reply>(ReplyDTO.class, replyPage);
		return MyResponse.ok(myReplyPage,true);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse get(@PathVariable("id") Long id)
	{
		Reply reply = replyService.getReply(id);
		if(reply == null)
		{
			String message = "回复不存在(id:" + id + ")";
			logger.warn(message);
			throw new RestException(HttpStatus.NOT_FOUND, message);
		}
		
		ReplyDTO replyDTO = BeanMapper.map(reply, ReplyDTO.class);
		
		return MyResponse.ok(replyDTO,true);
	}
	
	//consumes = MediaTypes.JSON 若设置了MediaTYpes 则在http的header中设置Content-Type为application/json才能成功映射，否则返回415
	@RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse create(@RequestBody Reply reply)//@RequestBody Reply reply
	{
		BeanValidators.validateWithException(validator,reply);
		
		replyService.saveReply(reply);
		
		System.out.println("reply restful create + ");
		
		//Reply result = replyService.getReply(reply.getId());
		
		ReplyDTO dto = BeanMapper.map(reply, ReplyDTO.class);

		return MyResponse.ok(dto,true);
	}
	
}
