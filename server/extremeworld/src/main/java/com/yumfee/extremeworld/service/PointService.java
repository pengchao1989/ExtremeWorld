package com.yumfee.extremeworld.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yumfee.extremeworld.config.PointType;
import com.yumfee.extremeworld.entity.Point;
import com.yumfee.extremeworld.entity.User;
import com.yumfee.extremeworld.repository.PointDao;
import com.yumfee.extremeworld.repository.UserDao;
import com.yumfee.extremeworld.util.DateUtils;


//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class PointService {

	@Autowired
	private PointDao pointDao;
	
	@Autowired UserDao userDao;
	
	public Point getPoint(long id){
		return pointDao.findOne(id);
	}
	
	public int getUserTotalPoint(long userId){
		User user = userDao.findById(userId);
		if (user != null) {
			return user.getPoint();
		}
		return 0;
	}
	
	//返回增加积分,0为没有增加，大于0为增加
	//规则：每日登陆赠送一次积分，每日前3条主题赠送积分，每日前5条回复赠送积分
	public int addPoint (PointType pointType, Long userId) {
		Point point = new Point();
		User user = userDao.findById(userId);
		if (user != null) {
			point.setUser(user);
			Date todayDate = DateUtils.getTodayBeginDate();
			if (pointType.equals(PointType.LOGIN)) {
				List<Point> todayLoginPointList = pointDao.findByTypeAndUserIdAndTime(userId,PointType.LOGIN.getType(),  todayDate);
				if (todayLoginPointList == null || todayLoginPointList.size() < 1) {
					point.setType(PointType.LOGIN.getType());
					point.setCount(PointType.LOGIN.getCount());
					point.setDes("每日登录积分 +" + String.valueOf(PointType.LOGIN.getCount()) );
				}else {
					return 0;
				}

			}else if(pointType.equals(PointType.TOPIC)){
				List<Point> todayTopicPointList = pointDao.findByTypeAndUserIdAndTime(userId,PointType.TOPIC.getType(),  todayDate);
				if (todayTopicPointList == null || todayTopicPointList.size() < 3) {
					point.setType(PointType.TOPIC.getType());
					point.setCount(PointType.TOPIC.getCount());
					point.setDes("发布主题 +" + String.valueOf(PointType.TOPIC.getCount()) );
				}else {
					return 0;
				}

			}else if(pointType.equals(PointType.REPLY)){
				List<Point> todayReplyPointList = pointDao.findByTypeAndUserIdAndTime(userId,PointType.REPLY.getType(),  todayDate);
				if (todayReplyPointList == null || todayReplyPointList.size() < 5) {
					point.setType(PointType.REPLY.getType());
					point.setCount(PointType.REPLY.getCount());
					point.setDes("回复主题 +" + String.valueOf(PointType.REPLY.getCount()));
				}else {
					return 0;
				}

			}else {
				return 0;
			}
		
			save(point);
			user.setPoint(user.getPoint() + point.getCount());
			userDao.save(user);
			return point.getCount();
		}
		
		return 0;
	}
	
	private Point save(Point point){
		return pointDao.save(point);
	}
	
	public Page<Point> getAll(Long userId, int pageNumber, int pageSize,String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return pointDao.findByUserId(userId, pageRequest);
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else {
			sort = new Sort(Direction.DESC, "id");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
}
