package com.yumfee.extremeworld.config;

public class TopicType {
	
	public static final String ALL = "all";
	public static final String MOOD = "mood";
	public static final String DISCUSS = "discuss";
	public static final String VIDEO = "video";
	public static final String S_VIDEO = "s_video";
	public static final String ACTIVITY = "activity";
	public static final String NEWS = "news";
	public static final String COURSE = "course";
	public static final String SITE = "site";
	
	
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
