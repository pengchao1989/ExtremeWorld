package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	public void setVideoDao(VideoDao videoDao)
	{
		this.videoDao = videoDao;
	}
}
