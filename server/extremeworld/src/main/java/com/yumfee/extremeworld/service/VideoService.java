package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Video;
import com.yumfee.extremeworld.repository.VideoDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class VideoService
{
	private VideoDao videoDao;
	

	public Video getVideo(long id)
	{
		return videoDao.findOne(id);
	}
	
	public void saveVideo(Video entity)
	{
		videoDao.save(entity);
	}
	
	public List<Video> getAll()
	{
		return (List<Video>) videoDao.findAll();
	}
	
	public Page<Video> getAllVideo(int pageNumber, int pageSize, String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return videoDao.findAll(pageRequest);
	}
	
	public Page<Video> getByUser(Long userId, int pageNumber, int pageSize, String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return videoDao.findByUserId(userId, pageRequest);
	}
	
	@Autowired
	public void setVideoDao(VideoDao videoDao)
	{
		this.videoDao = videoDao;
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
