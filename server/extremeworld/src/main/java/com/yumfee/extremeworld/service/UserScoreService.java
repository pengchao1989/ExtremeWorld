package com.yumfee.extremeworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

	public Page<UserScore> getRankingListByHobbyId(int pageNumber, int pageSize,String sortType, Long hobbyId){
		
		PageRequest pageable = buildPageRequest(pageNumber, pageSize, sortType);
		
		return userScoreDao.findByHobbyId(hobbyId, pageable);
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "score");
		}else{
			sort = new Sort(Direction.DESC, "id");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
}
