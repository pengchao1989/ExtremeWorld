package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
	
	public List<Reply> getAll()
	{
		return (List<Reply>) replyDao.findAll();
	}
	
	public Page<Reply> getAll(int pageNumber, int pageSize)
	{
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize);
		return replyDao.findAll(pageRequest);
	}
	
	public Page<Reply> getAll(Long topicId, int pageNumber, int pageSize)
	{
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize);
		return replyDao.findByTopicId(topicId,pageRequest);
	}
	
	public void saveReply(Reply entity)
	{
		replyDao.save(entity);
	}
	

}
