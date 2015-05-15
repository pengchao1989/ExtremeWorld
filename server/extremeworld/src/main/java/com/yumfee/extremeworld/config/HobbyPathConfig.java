package com.yumfee.extremeworld.config;

public class HobbyPathConfig {

	public static Long getHobbyId(String hobby)
	{
		Long hobbyId = 0L;
		
		if(hobby == null)
		{
			hobbyId = 0L;
		}
		else if(hobby.equals("skateboard"))
		{
			hobbyId = 1L;
		}
		else if(hobby.equals("parkour"))
		{
			hobbyId = 2L;
		}
		else if(hobby.equals("bicycle"))
		{
			hobbyId = 3L;
		}
		else if(hobby.equals("roller-skating"))
		{
			hobbyId = 4L;
		}
		else if(hobby.equals("snowboard"))
		{
			hobbyId = 5L;
		}
		
		return hobbyId;
	}
}
