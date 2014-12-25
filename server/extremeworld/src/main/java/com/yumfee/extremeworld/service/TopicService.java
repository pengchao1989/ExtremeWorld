package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.repository.TopicDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class TopicService
{
	private TopicDao topicDao;
	
	public Topic getTopic(Long id)
	{
		return topicDao.findOne(id);
	}
	
	public void saveTopic(Topic entity)
	{
		topicDao.save(entity);
	}
	
	public List<Topic> getAllTopic()
	{
		return (List<Topic>) topicDao.findAll();
	}
	
	@Autowired
	public void setTopicDao(TopicDao topicDao)
	{
		this.topicDao = topicDao;
	}
}
