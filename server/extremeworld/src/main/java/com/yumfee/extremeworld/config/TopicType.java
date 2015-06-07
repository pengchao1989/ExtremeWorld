package com.yumfee.extremeworld.config;

public class TopicType {
	
	public static final String mood = "mood";
	public static final String discuss = "discuss";
	public static final String video = "video";
	
	public static final String magicExplain = "explain";
	public static final String magicQuestion = "question";

	public static String getTypeString(int type)
	{
		String typeString = "mood";
		
		switch(type)
		{
		case 2:
			typeString = mood;
			break;
			
		case 3:
			typeString = discuss;
			break;
		}
		
		return typeString;
	}
}
