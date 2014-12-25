package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Reply;
import com.yumfee.extremeworld.repository.ReplyDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class ReplyService
{
	@Autowired
	private ReplyDao replyDao;

	public Reply getReply(long id)
	{
		return replyDao.findOne(id);
	}
	
}
