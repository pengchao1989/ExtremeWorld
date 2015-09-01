package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.config.RemindType;
import com.yumfee.extremeworld.entity.Remind;
import com.yumfee.extremeworld.entity.Reply;
import com.yumfee.extremeworld.entity.SubReply;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.push.PushManage;
import com.yumfee.extremeworld.push.PushMessageType;
import com.yumfee.extremeworld.repository.RemindDao;
import com.yumfee.extremeworld.repository.ReplyDao;
import com.yumfee.extremeworld.repository.SubReplyDao;
import com.yumfee.extremeworld.repository.TopicDao;
import com.yumfee.extremeworld.repository.UserDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class SubReplyService {

	@Autowired
	private SubReplyDao subReplyDao;
	
	@Autowired
	private ReplyDao replyDao;
	
	@Autowired
	private TopicDao topicDao;
	
	@Autowired
	private RemindDao remindDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PushManage pushManage;
	
	public SubReply getSubReply(long id){
		return subReplyDao.findOne(id);
	}
	
	public Page<SubReply> getAll(long replyId, int pageNumber, int pageSize){
		PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize);
		return subReplyDao.findByReplyId(replyId, pageRequest);
	}
	
	public void saveSubReply(SubReply subReply){
		
		subReplyDao.save(subReply);
		
		
		Reply reply = replyDao.findOne(subReply.getReply().getId());
		reply.setSubReplyCount(reply.getSubReplyCount() + 1);
		replyDao.save(reply);
		
		Topic topic = reply.getTopic();
		topic.setAllReplyCount(topic.getAllReplyCount() + 1);
		topicDao.save(topic);
		
		User speaker = userDao.findById(subReply.getUser().getId());
		SubReply preSubReply = null;
		if(subReply.getPreSubReply() != null){
			preSubReply = subReplyDao.findOne(subReply.getPreSubReply().getId());
		}
		 
		// TODO下面代码应异步处理
		
		// 1.给reply作者添加提醒
		Remind remind = new Remind();
		remind.setType(RemindType.TYPE_SUB_REPLY);
		remind.setContent(subReply.getContent());
		remind.setSpeaker(speaker);
		
		remind.setTargetId(reply.getId());
		remind.setTargetType(RemindType.TARGET_TYPE_REPLY);
		remind.setTargetContent(reply.getContent());
		remind.setListener(reply.getUser());
		
		if(preSubReply == null){
			
			if(reply.getUser().getId() != subReply.getUser().getId()){
				remindDao.save(remind);
				//push
				pushManage.pushMessage(reply.getUser(), PushMessageType.REMIND, remind);
			}
			
		} else{
			
			// 2.如果存在preSubReply，则对preSubReply作者进添加提醒
			if(subReply.getUser().getId() != preSubReply.getUser().getId()){
				Remind preRemind = new Remind();
				preRemind.setType(RemindType.TYPE_SUB_REPLY);
				preRemind.setContent(subReply.getContent());
				preRemind.setSpeaker(speaker);
				
				
				preRemind.setTargetId(reply.getId());
				preRemind.setTargetType(RemindType.TARGET_TYPE_SUB_REPLY);
				preRemind.setTargetContent(preSubReply.getContent());
				preRemind.setListener(preSubReply.getUser());
				
				remindDao.save(preRemind);
				//push
				pushManage.pushMessage(preSubReply.getUser(), PushMessageType.REMIND, preRemind);
			}
			
			if((reply.getUser().getId() != preSubReply.getUser().getId())
					&&(reply.getUser().getId() != subReply.getUser().getId())){
						remindDao.save(remind);
						//push
						pushManage.pushMessage(reply.getUser(), PushMessageType.REMIND, remind);
			}
		}

	}
}
