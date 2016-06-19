package com.yumfee.extremeworld.config;

public class TopicType {
	
	public static final String ALL = "all";
	public static final String MOOD = "mood";		//心情
	public static final String DISCUSS = "discuss";	//讨论
	public static final String VIDEO = "video";		//视频
	public static final String S_VIDEO = "s_video";	//短视频
	public static final String ACTIVITY = "activity";//活动
	public static final String NEWS = "news";		//新闻
	public static final String COURSE = "course";	//教学
	public static final String SITE = "site";		//场地信息
	public static final String ACHV = "achv";		//成就 Achievement
	public static final String CHALLENGE = "challenge";//挑战
	
	
	public static final String magicExplain = "explain";
	public static final String magicQuestion = "question";
	public static final String magicSB = "sb";
	public static final String magicAchievement = "achievement";

	public static String getTypeString(int type)
	{
		
		switch(type)
		{
		case 2:
			return MOOD;
			
		case 3:
			return DISCUSS;
			
		case 4:
			return VIDEO;
		
		case 5:
			return S_VIDEO;
		
		case 6:
			return ACTIVITY;
			
		case 7:
			return NEWS;
		case 8:
			return COURSE;
		
		default:
				
			return MOOD;
			
		}

	}
}
