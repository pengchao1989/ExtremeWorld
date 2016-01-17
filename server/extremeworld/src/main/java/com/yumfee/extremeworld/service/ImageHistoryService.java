package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.ImageHistory;
import com.yumfee.extremeworld.repository.ImageHistoryDao;

@Component
@Transactional
public class ImageHistoryService {
	@Autowired
	private ImageHistoryDao imageHistoryDao;
	
	public void save(ImageHistory imageHistory){
		imageHistoryDao.save(imageHistory);
	}
}
