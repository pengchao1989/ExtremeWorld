package com.yumfee.extremeworld.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.entity.ReferenceAvatar;
import com.yumfee.extremeworld.repository.ReferenceAvatarDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class ReferenceAvatarService {

	@Autowired
	ReferenceAvatarDao referenceAvatarDao;

	public ReferenceAvatar getRandom() {

		List<ReferenceAvatar> referenceAvatarList = (List<ReferenceAvatar>) referenceAvatarDao.findAll();
		if (referenceAvatarList.size() > 0) {
			Random rand = new Random();
			ReferenceAvatar randomOne = referenceAvatarList.get(rand
					.nextInt(referenceAvatarList.size()));
			return randomOne;
		}
		return null;
	}
	
}
