package com.yumfee.extremeworld.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Topic;

public interface TopicDao extends PagingAndSortingRepository<Topic, Long>
{
/*	@Query("SELECT tb_topic FROM tb_course_topic  LEFT JOIN tb_topic ON tb_course_topic.topic_id = tb_topic.id WHERE tb_course_topic.course_id = ?")
	public Page<Topic> findByCourseId(Long courseId,Pageable pageable);*/
	public Page<Topic> findByCourseIdAndMagicTypeAndStatus(Long courseId,String magicType, int status ,Pageable pageable);
	public Page<Topic> findByUserIdInAndStatus(Collection<Long> ids, int status, Pageable pageable);
	
	public Page<Topic> findByUserIdAndStatus(Long user, int status, Pageable pageable);
	public Page<Topic> findByUserIdAndStatusAndMediaWrapNotNull(Long user, int status,  Pageable pageable);
	
	public Page<Topic> findByTypeAndStatus(String type, int status, Pageable pageable);
	
	
	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND t.status=?")
	public Page<Topic> findByHobby(Long hobbyId, int status, Pageable pageable);
	
	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND type=? AND status=?")
	public Page<Topic> findByHobbyAndType(Long hobbyId, String type, int status, Pageable pageable);
	
	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND type=? AND t.taxonomy.id=? AND status=?")
	public Page<Topic> findByHobbyAndTypeAndTaxonomy(Long hobbyId,  String type, Long taxonomyId, int status,Pageable pageable);
	
	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND status=? AND t.fine=1")
	public Page<Topic> findByHobbyAndFine(Long hobbyId, int status, Pageable pageable);
	
	
}
