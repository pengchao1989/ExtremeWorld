package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.config.RemindType;
import com.yumfee.extremeworld.entity.Remind;
import com.yumfee.extremeworld.entity.Reply;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.push.PushManage;
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
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PushManage pushManage;

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
	
	public void saveReply(Reply reply) {
		
		Topic topic = topicDao.findOne(reply.getTopic().getId());
		reply.setFloor(topic.getReplyCount() + 1);
		replyDao.save(reply);
		topic.setReplyCount(topic.getReplyCount() + 1);
		topic.setAllReplyCount(topic.getAllReplyCount() + 1);
		topicDao.save(topic);

		// TODO下面代码应异步处理

		// 在此处理提醒数据
		Remind remind = new Remind();
		remind.setType(RemindType.TYPE_REPLY);
		remind.setContent(reply.getContent());
		remind.setSpeaker(reply.getUser());

		remind.setTargetId(topic.getId());
		remind.setTargetType(RemindType.TARGET_TYPE_TOPIC);
		remind.setTargetContent(topic.getTitle());
		remind.setListener(topic.getUser());

		if (reply.getUser().getId() != topic.getUser().getId()) {
			remindDao.save(remind);
			
			User speaker = userDao.findById(reply.getUser().getId());

			// 在此处理推送
			pushManage.pushNotice(topic.getUser(), speaker.getName() + "回复了你!", remind.getContent());
			
		}

	}

}
