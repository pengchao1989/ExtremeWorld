package com.yumfee.extremeworld.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.modules.nosql.redis.JedisTemplate;
import com.yumfee.extremeworld.modules.nosql.redis.MyJedisExecutor;
import com.yumfee.extremeworld.modules.nosql.redis.pool.JedisPool;
import com.yumfee.extremeworld.modules.nosql.redis.pool.JedisPoolBuilder;
import com.yumfee.extremeworld.repository.TopicDao;
import com.yumfee.extremeworld.repository.UserDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class TopicService
{
	@Autowired
	private TopicDao topicDao;
	@Autowired
	private UserDao userInfoDao;
	
	
	public Topic getTopic(Long id)
	{
		Topic topic = null;
		
/*		topic = MyJedisExecutor.get("topic:"+id, Topic.class);
		
		if(topic == null)
		{
			System.out.println("topic == null");
			topic = topicDao.findOne(id);
			MyJedisExecutor.set("topic:"+id, topic);
			
		}

		topic.setViewCount(MyJedisExecutor.incr("topic:view:"+id).intValue());*/
		
		
		topic = topicDao.findOne(id);
		return topic;
	}
	
	public void saveTopic(Topic entity)
	{
		entity.setExcerpt(entity.getTitle());
		topicDao.save(entity);
		//MyJedisExecutor.set("topic:"+entity.getId(), entity);
	}
	
	public List<Topic> getAllTopic()
	{
		return (List<Topic>) topicDao.findAll();
	}
	
	public Page<Topic> getAllTopic(int pageNumber, int pageSize,
			String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return topicDao.findAll(pageRequest);
	}
	
	public Page<Topic> getAllTopicByCourse(Long courseId, int pageNumber, int pageSize,String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		
		return topicDao.findByCourseId(courseId,pageRequest);
	}
	
	public Page<Topic> getTopicByfollowings(Long id, int pageNumber, int pageSize,String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		
		List<Long> followingIds = new ArrayList<Long>();
		List<User> followings = userInfoDao.findOne(id).getFollowings();
		for(User userInfo : followings)
		{
			followingIds.add(userInfo.getId());
		}
		return topicDao.findByUserIdIn(followingIds, pageRequest);
	}
	
	
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("title".equals(sortType)) {
			sort = new Sort(Direction.ASC, "title");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
}
