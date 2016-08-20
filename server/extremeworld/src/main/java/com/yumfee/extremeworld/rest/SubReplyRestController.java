package com.yumfee.extremeworld.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.yumfee.extremeworld.entity.SubReply;
import com.yumfee.extremeworld.rest.dto.MyPage;
import com.yumfee.extremeworld.rest.dto.MyResponse;
import com.yumfee.extremeworld.rest.dto.SubReplyDTO;
import com.yumfee.extremeworld.service.SubReplyService;

@RestController
@RequestMapping(value = "/api/secure/v1/sub_reply")
public class SubReplyRestController {
	private static Logger logger = LoggerFactory.getLogger(SubReplyRestController.class);
	
	private static final String PAGE_SIZE = "50";
	
	@Autowired
	SubReplyService subReplyService;
	
	@Autowired
	private Validator validator;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(@RequestParam(value = "replyId") Long replyId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		Page<SubReply> subReplyPage = subReplyService.getAll(replyId,pageNumber, pageSize);
		
		MyPage<SubReplyDTO,SubReply> mySubReplyPage = new MyPage();
		mySubReplyPage.setTotalPages(subReplyPage.getTotalPages());
		mySubReplyPage.setTotalElements(subReplyPage.getTotalElements());
		mySubReplyPage.setCurPage(subReplyPage.getNumber());
		
		List<SubReplyDTO> subReplyDTOList = new ArrayList<SubReplyDTO>();
		for(SubReply subReply : subReplyPage.getContent()){
			SubReplyDTO subReplyDTO = BeanMapper.map(subReply, SubReplyDTO.class);
			if(subReply.getPreSubReply() != null){
				SubReplyDTO targetSubReplyDTO = BeanMapper.map(subReply.getPreSubReply(), SubReplyDTO.class);
				subReplyDTO.setTarget(targetSubReplyDTO);
			}
			subReplyDTOList.add(subReplyDTO);
		}
		mySubReplyPage.setContents(subReplyDTOList);
		
		return MyResponse.ok(mySubReplyPage,true);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse create(@RequestBody SubReply subReply){
		
		BeanValidators.validateWithException(validator,subReply);
		
		subReplyService.saveSubReply(subReply);
		SubReplyDTO subReplyDTO = BeanMapper.map(subReply, SubReplyDTO.class);
		
		return MyResponse.ok(subReplyDTO,true);
		
	}
	
}
