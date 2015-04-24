package com.yumfee.extremeworld.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.Danmu;
import com.yumfee.extremeworld.repository.DanmuDao;


//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class DanmuService {

	private DanmuDao danmuDao;
	
	public Danmu getDanmu(Long id){
		
		return danmuDao.findOne(id);
	}
	
	public void SaveDanmu(Danmu danmu)
	{
		danmuDao.save(danmu);
	}

	public List<Danmu> getAllDanmuByVideoId(Long videoId)
	{
		return danmuDao.findByVideoId(videoId);
	}
	@Autowired
	public void setDanmuDao(DanmuDao danmuDao) {
		this.danmuDao = danmuDao;
	}
	
	
	
}
