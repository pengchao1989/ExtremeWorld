package com.yumfee.extremeworld.modules.nosql.redis;

import java.util.ArrayList;
import java.util.List;

import org.springside.modules.mapper.JsonMapper;

import com.yumfee.extremeworld.modules.nosql.redis.pool.JedisPool;
import com.yumfee.extremeworld.modules.nosql.redis.pool.JedisPoolBuilder;

public class MyJedisExecutor {

	private static JedisPool pool = new JedisPoolBuilder().setUrl("direct://localhost:6379?poolSize=" + 20 +"&poolName=abc").buildPool();
	static JedisTemplate jedisTemplate = new JedisTemplate(pool);
	private static JsonMapper jsonMapper = new JsonMapper();
	
	public static boolean set(String key,Object object)
	{
		
		jedisTemplate.set(key, jsonMapper.toJson(object));
		
		return true;
	}
	
	public static <T> T get(String key, Class<T> clazz){
		
		String json = jedisTemplate.get(key);
	
		return jsonMapper.fromJson(json, clazz);
	}
	
	public static Long incr(String key)
	{
		return jedisTemplate.incr(key);
	}
	
	public static Long lpush(String key, String value)
	{
		return jedisTemplate.lpush(key, value);
		
	}
	 
	public static <T> boolean rpushList(String key, List<T> list)
	{
		if(list == null || list.size() == 0)
		{
			return false;
		}
		
		for(T item : list)
		{
			jedisTemplate.lpush(key, jsonMapper.toJson(item));
		}
		
		return true;
	}
	
	public static List<String> lrange(String key)
	{
		return jedisTemplate.lrange(key, 0, -1);
	}
	
	public static <T> List<T> lrangeList(String key, Class<T> clazz)
	{
		List<T> listItem = new ArrayList<T>();
		
		List<String> listItemString = jedisTemplate.lrange(key, 0, -1);
		for(String itemString : listItemString)
		{
			listItem.add(jsonMapper.fromJson(itemString, clazz));
		}
		
		return listItem;
		
	}
}
