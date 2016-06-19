package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.TopicScore;
import com.yumfee.extremeworld.repository.TopicScoreDao;

@Component
@Transactional
public class TopicScoreService {
	
	private TopicScoreDao topicScoreDao;

	public TopicScore findByTopicIdAndUserId(Long topicId, Long userId){
		return topicScoreDao.findByTopicIdAndUserId(topicId, userId);
	}

	public void save(TopicScore topicScore){
		topicScoreDao.save(topicScore);
	}
	
	public Page<TopicScore> getAll(int pageNumber, int pageSize,String sortType){
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return topicScoreDao.findAll(pageRequest);
	}
	
	public double getTopicAvgScore(Long topicId){
		return topicScoreDao.getTopicAvgScore(topicId);
	}

	@Autowired
	public void setTopicScoreDao(TopicScoreDao topicScoreDao) {
		this.topicScoreDao = topicScoreDao;
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}else{
			sort = new Sort(Direction.DESC, "id");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

}
