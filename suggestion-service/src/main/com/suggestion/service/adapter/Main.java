package com.suggestion.service.adapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.suggestion.service.service.command.CompanyCreatedCommand;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;

public class Main {

	public static void main(String[] args) {
		Set<String> suggestionIds = new HashSet<>();
		JedisPooled jedis = new JedisPooled("localhost", 6379);
		jedis.flushAll();
//		jedis.rpush("+1", "1");
//		jedis.rpush("+1", "2");
//		jedis.rpush("+1", "3");
//		jedis.rpush("1", "2");
//		
//		String value = jedis.rpop("+" + "1");
//		while(value != null && value != "") {
//			suggestionIds.add(value);
//			value = jedis.rpop("+" + "1");
//		}
//		
//		value = jedis.rpop("1");
//		while(value != null && value != "") {
//			suggestionIds.remove(value);
//			value = jedis.rpop("1");
//		}
//		
//		for(String str: suggestionIds) {
//			System.out.println(str);
//		}

//		return suggestionIds.stream().collect(Collectors.toList());
//        JedisPool pool = new JedisPool("localhost", 6379);
//
//        try (Jedis jedis = pool.getResource()) {
//            // Store & Retrieve a simple string
//            jedis.set("foo", "bar");
//            System.out.println(jedis.get("foo")); // prints bar
//            
//            // Store & Retrieve a HashMap
//            Map<String, String> hash = new HashMap<>();;
//            hash.put("name", "John");
//            hash.put("surname", "Smith");
//            hash.put("company", "Redis");
//            hash.put("age", "29");
//            jedis.hset("user-session:123", hash);
//            System.out.println(jedis.hgetAll("user-session:123"));
//            // Prints: {name=John, surname=Smith, company=Redis, age=29}
//        }
    }

}
