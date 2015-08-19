package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.CloudpushNoticeAndroidRequest;
import com.taobao.api.response.CloudpushNoticeAndroidResponse;
import com.yumfee.extremeworld.config.ClientConfigManage;
import com.yumfee.extremeworld.entity.ClientConfig;
import com.yumfee.extremeworld.entity.Remind;
import com.yumfee.extremeworld.entity.Reply;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
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
	private ClientConfigManage clientConfigManage;

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
			
			User Speaker = userDao.findById(reply.getUser().getId());

			// 在此处理推送
			ClientConfig clientConfig = clientConfigManage.getCilentConfig(3);
			String appkey = clientConfig.getBaichuanAppKey();
			String secret = clientConfig.getBaichuanAppSecret();
			String url = "http://gw.api.taobao.com/router/rest";

			TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
	        CloudpushNoticeAndroidRequest req=new CloudpushNoticeAndroidRequest();
	        req.setTitle(Speaker.getName() + "回复了你!");
	        req.setSummary(remind.getContent());
	        req.setTarget("account");
	        req.setTargetValue(String.valueOf(topic.getUser().getId()));
	        try {
	            CloudpushNoticeAndroidResponse response = client.execute(req);
	            if(response.isSuccess()){
	                System.out.println("push  notice is success!");
	            }
	        }
	        catch (Exception e){
	            System.out.println("push notice is error!");
	        }
		}

	}

}
