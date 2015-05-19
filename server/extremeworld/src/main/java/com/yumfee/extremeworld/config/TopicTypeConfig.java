package com.yumfee.extremeworld.config;

public class TopicTypeConfig {

	public static String getTypeString(int type)
	{
		String typeString = "mood";
		
		switch(type)
		{
		case 2:
			
			break;
			
		case 3:
			typeString = "discuss";
			break;
		}
		
		return typeString;
	}
}
