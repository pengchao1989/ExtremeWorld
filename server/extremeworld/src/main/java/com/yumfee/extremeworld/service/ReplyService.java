package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Remind;
import com.yumfee.extremeworld.entity.Reply;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.repository.RemindDao;
import com.yumfee.extremeworld.repository.ReplyDao;
import com.yumfee.extremeworld.repository.TopicDao;
import com.yumfee.extremeworld.repository.UserDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class ReplyService
{
	@Autowired
	private ReplyDao replyDao;
	
	@Autowired
	private RemindDao remindDao;
	
	@Autowired 
	private TopicDao topicDao;

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
	
	public void saveReply(Reply reply)
	{
		replyDao.save(reply);
		
		//在此处理提醒数据
		Remind remind = new Remind();
		remind.setTargetType("topic");
		remind.setContent(reply.getContent());
		remind.setTargetId(reply.getTopic().getId());
		remind.setSpeaker(reply.getUser());
		
		
		Topic topic = topicDao.findOne(reply.getTopic().getId());
		remind.setTargetContent(topic.getTitle());
		remind.setListener(topic.getUser());
		
		remindDao.save(remind);
		
	}
	

}
