package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

import com.yumfee.extremeworld.entity.Remind;
import com.yumfee.extremeworld.entity.Reply;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.repository.RemindDao;
import com.yumfee.extremeworld.repository.ReplyDao;
import com.yumfee.extremeworld.repository.TopicDao;

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
	
	public void saveReply(Reply reply) {
		replyDao.save(reply);

		// TODO下面代码应异步处理

		// 在此处理提醒数据
		Remind remind = new Remind();
		remind.setTargetType("topic");
		remind.setContent(reply.getContent());
		remind.setTargetId(reply.getTopic().getId());
		remind.setSpeaker(reply.getUser());

		Topic topic = topicDao.findOne(reply.getTopic().getId());
		remind.setTargetContent(topic.getTitle());
		remind.setListener(topic.getUser());

		if (reply.getUser().getId() != topic.getUser().getId()) {
			remindDao.save(remind);

			// 在此处理推送
			JPushClient jpushClient = new JPushClient(
					"80a458ad8be7998a79b9af85", "4798383b7d7bdee3a2db3356", 3);
			PushPayload payload = PushPayload
					.newBuilder()
					.setPlatform(Platform.android_ios())
					.setAudience(
							Audience.alias(remind.getListener().getId()
									.toString()))
					.setMessage(
							Message.newBuilder()
									.setMsgContent(
											remind.getTargetContent() + " 有新回复")
									.addExtra("targetId", remind.getTargetId())
									.build()).build();
			try {
				PushResult result = jpushClient.sendPush(payload);

			} catch (APIConnectionException e) {
				// Connection error, should retry later

			} catch (APIRequestException e) {
				// Should review the error, and fix the request
			}
		}

	}

}
