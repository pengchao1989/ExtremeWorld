package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.UserScore;

public interface UserScoreDao extends PagingAndSortingRepository<UserScore, Long>{

	public UserScore findByUserIdAndHobbyId(Long userId, Long hobbyId);
}
