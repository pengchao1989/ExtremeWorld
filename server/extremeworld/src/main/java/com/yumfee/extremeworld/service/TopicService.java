package com.yumfee.extremeworld.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.yumfee.extremeworld.config.TopicStatus;
import com.yumfee.extremeworld.entity.Media;
import com.yumfee.extremeworld.entity.MediaWrap;
import com.yumfee.extremeworld.entity.Topic;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.modules.nosql.redis.JedisTemplate;
import com.yumfee.extremeworld.modules.nosql.redis.MyJedisExecutor;
import com.yumfee.extremeworld.modules.nosql.redis.pool.JedisPool;
import com.yumfee.extremeworld.modules.nosql.redis.pool.JedisPoolBuilder;
import com.yumfee.extremeworld.repository.MediaWrapDao;
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
	private MediaWrapDao mediaWrapDao;
	
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
	
	public void saveTopic(Topic entity){
		String content = entity.getContent();
		if(content.length() > 160)
		{
			entity.setExcerpt(content.substring(0, 159) + "....");
		}
		else
		{
			entity.setExcerpt(content);
		}
		topicDao.save(entity);
	}
	
	public void saveHtmlTopic(Topic entity)
	{
		
		//提交上来的topic.content都是标准html内容
		//对内容进行分割
		//图片提取出来放进mediawrap
		//构建excerpt用做首页展示
		
		Document content = Jsoup.parse(entity.getContent());
		Elements images = content.getElementsByTag("img");
		
		
		//构建mediawrap
		MediaWrap mediaWrap = new MediaWrap();
		List<Media> medias = new ArrayList<Media>();
		mediaWrap.setMedias(medias);
		for(Element img : images)
		{
			//尼玛web端上传上来的img多了个_src的属性，在这里删除一下，其它地方不好搞。TODO 需优化
			img.removeAttr("_src");
			
			String src = img.attr("src");
			String alt = img.attr("alt");
			
			
			Media imgMedia = new Media();
			imgMedia.setType("img");
			imgMedia.setDes(alt);
			imgMedia.setPath(src);
			
			mediaWrap.getMedias().add(imgMedia);
		}
		if(medias.size() > 0){
			mediaWrapDao.save(mediaWrap);
			entity.setMediaWrap(mediaWrap);
		}
		

		
		
		//去掉img标签后，再提取前160个字符，作为excerpt
		System.out.println("content.text=" + content.text());

		if(content.text().length() > 160)
		{
			entity.setExcerpt(content.text().substring(0, 159) + "....");
		}
		else
		{
			entity.setExcerpt(content.text());
		}
		
		topicDao.save(entity);
		//MyJedisExecutor.set("topic:"+entity.getId(), entity);
		
	}
	
	public Page<Topic> getAllTopic(int pageNumber, int pageSize,
			String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return topicDao.findAll(pageRequest);
	}
	
	public Page<Topic> getAllTopicByCourseAndMagicType(Long courseId, String magicType, int pageNumber, int pageSize,String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		
		return topicDao.findByCourseIdAndMagicTypeAndStatus(courseId, magicType,0, pageRequest);
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
		return topicDao.findByUserIdInAndStatus(followingIds, TopicStatus.PUBLIC, pageRequest);
	}
	
	public Page<Topic> getTopicByUser(Long userId, int pageNumber, int pageSize,String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		
		return topicDao.findByUserIdAndStatus(userId, TopicStatus.PUBLIC ,pageRequest);
	}
	
	public Page<Topic> getTopicByUserAndMediaWrapNotNull(Long userId , int pageNumber, int pageSize,String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
	
		return topicDao.findByUserIdAndStatusAndMediaWrapNotNull(userId, TopicStatus.PUBLIC ,pageRequest);
	}
	
	public Page<Topic> getTopicByType(String type, int pageNumber, int pageSize, String sortType){
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return topicDao.findByTypeAndStatus(type,TopicStatus.PUBLIC,pageRequest);
	}
	
	public Page<Topic> getTopicByHobby(Long hobbyId, int pageNumber, int pageSize,String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		
		if(hobbyId == 0L)
		{
			return topicDao.findAll(pageRequest);
		}
		
		return topicDao.findByHobby(hobbyId, TopicStatus.PUBLIC ,pageRequest);
	}
	
	public Page<Topic> getTopicByHobbyAndType(Long hobbyId, String type, int pageNumber, int pageSize, String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return topicDao.findByHobbyAndType(hobbyId, type, TopicStatus.PUBLIC, pageRequest);
	}
	
	public Page<Topic> getTopicByHobbyAndTypeAndTaxonomy(Long hobbyId, String type, Long taxonomyId, int pageNumber, int pageSize, String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return topicDao.findByHobbyAndTypeAndTaxonomy(hobbyId, type, taxonomyId, TopicStatus.PUBLIC ,pageRequest);
	}
	
	public Page<Topic> getFineTopic(Long hobbyId, int pageNumber, int pageSize, String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return topicDao.findByHobbyAndFine(hobbyId,TopicStatus.PUBLIC,pageRequest);
		
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
		}else if("agree".equals(sortType)){
			sort = new Sort(Direction.DESC, "agreeCount");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
}
