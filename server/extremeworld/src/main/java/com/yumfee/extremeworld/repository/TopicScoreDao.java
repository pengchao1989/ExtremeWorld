package com.yumfee.extremeworld.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.TopicScore;

public interface TopicScoreDao extends PagingAndSortingRepository<TopicScore, Long> {

	public TopicScore findByTopicIdAndUserId(Long topicId, Long userId);
	
	public Page<TopicScore> findByTopicId(Long topicId, Pageable page);
	
	@Query("SELECT AVG(ts.score) FROM TopicScore ts WHERE ts.topic.id=?")
	public double getTopicAvgScore(Long topicId);
	
	@Query("SELECT COUNT(ts.id) FROM TopicScore ts WHERE ts.topic.id=?")
	public int getTopicScoreCount(Long topicId);
}
