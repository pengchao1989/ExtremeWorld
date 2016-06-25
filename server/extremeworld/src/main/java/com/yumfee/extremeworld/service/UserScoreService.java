package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.UserScore;
import com.yumfee.extremeworld.repository.UserScoreDao;

@Component
@Transactional
public class UserScoreService {

	@Autowired
	private UserScoreDao userScoreDao;
	
	public UserScore findByUserIdAndHobbyId(Long userId, Long hobbyId){
		return userScoreDao.findByUserIdAndHobbyId(userId, hobbyId);
	}
	
	public UserScore save(UserScore userScore){
		return userScoreDao.save(userScore);
	}
}
