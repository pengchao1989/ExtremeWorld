package com.yumfee.extremeworld.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Topic;

public interface TopicDao extends PagingAndSortingRepository<Topic, Long>
{
/*	@Query("SELECT tb_topic FROM tb_course_topic  LEFT JOIN tb_topic ON tb_course_topic.topic_id = tb_topic.id WHERE tb_course_topic.course_id = ?")
	public Page<Topic> findByCourseId(Long courseId,Pageable pageable);*/
	public Page<Topic> findByCourseId(Long courseId,Pageable pageable);
	public Page<Topic> findByUserIdIn(Collection<Long> ids, Pageable pageable);
	
	public Page<Topic> findByUserId(Long user, Pageable pageable);
	public Page<Topic> findByUserIdAndMediaWrapNotNull(Long user,  Pageable pageable);
}
