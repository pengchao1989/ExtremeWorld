package com.yumfee.extremeworld.service;

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
import com.yumfee.extremeworld.modules.nosql.redis.JedisTemplate;
import com.yumfee.extremeworld.modules.nosql.redis.pool.JedisPool;
import com.yumfee.extremeworld.modules.nosql.redis.pool.JedisPoolBuilder;
import com.yumfee.extremeworld.repository.TopicDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class TopicService
{
	private TopicDao topicDao;
	
	private JedisTemplate jedisTemplate;
	
	private JsonMapper jsonMapper = new JsonMapper();
	
	public Topic getTopic(Long id)
	{
		Topic topic = null;
		
		//redis view count inc
		JedisPool pool = new JedisPoolBuilder().setUrl("direct://localhost:6379?poolSize=" + 20 +"&poolName=abc").buildPool();
		jedisTemplate = new JedisTemplate(pool);
		
		String topicString = jedisTemplate.get("topic:"+id);
		if(topicString == null)
		{
			System.out.println("topicString == null");
			topic = topicDao.findOne(id);
			topicString =  jsonMapper.toJson(topic);
			System.out.println("topicString2 =" + topicString);
			jedisTemplate.set("topic:"+id, topicString );
		}
		else 
		{
			System.out.println("topicString =" + topicString);
			topic = jsonMapper.fromJson(topicString, Topic.class);
		}
		
		
		return topic;
	}
	
	public void saveTopic(Topic entity)
	{
		entity.setExcerpt(entity.getTitle());
		topicDao.save(entity);
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
	
	@Autowired
	public void setTopicDao(TopicDao topicDao)
	{
		this.topicDao = topicDao;
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
