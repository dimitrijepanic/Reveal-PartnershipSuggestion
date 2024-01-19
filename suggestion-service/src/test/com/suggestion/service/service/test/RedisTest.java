package com.suggestion.service.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.suggestion.service.domain.Company;
import com.suggestion.service.domain.Response;
import com.suggestion.service.domain.Suggestion;
import com.suggestion.service.domain.TimerEvent;
import com.suggestion.service.service.CacheService;
import com.suggestion.service.service.RedisService;

import redis.clients.jedis.JedisPooled;

public class RedisTest {

	@Test
	public void connectTest() {
		CacheService cacheService = new RedisService();
		Response response = cacheService.connect();
		
		assertEquals(response.ordinal(), 0);
	}
	
	@Test
	public void addCompanySuggestionsTest() {
		CacheService cacheService = new RedisService();
		Response response = cacheService.connect();
		assertEquals(response.ordinal(), 0);
		
		List<String> suggestions = new ArrayList<>();
		Company company = new Company("3", "Reveal", "Software Engineering", "France");
		
		suggestions.add("1");
		suggestions.add("2");
		company.setSuggestions(suggestions);
		
		cacheService.addCompanySuggestions(company);
		
		JedisPooled jedis = new JedisPooled("localhost", 6379);
		String value1 = jedis.rpop("+3");
		String value2 = jedis.rpop("+3");
		
		assertNotNull(value1);
		assertNotNull(value2);
		assertEquals(value1, "2");
		assertEquals(value2, "1");
		
	}
	
	@Test
	public void addTimerEventsTest() {
		CacheService cacheService = new RedisService();
		Response response = cacheService.connect();
		assertEquals(response.ordinal(), 0);
		
		List<TimerEvent> timerEvents = new LinkedList<>();
		TimerEvent te1 = new TimerEvent(1, 3600);
		TimerEvent te2 = new TimerEvent(3, 83600);
		Company company = new Company("3", "Reveal", "Software Engineering", "France");
		
		timerEvents.add(te1);
		timerEvents.add(te2);
		
		cacheService.addTimerEvents(company, timerEvents);
		
		JedisPooled jedis = new JedisPooled("localhost", 6379);
		String value1 = jedis.rpop("-3");
		String value2 = jedis.rpop("-3");
		
		assertNotNull(value1);
		assertNotNull(value2);
		assertEquals(value1, "1:3600");
		assertEquals(value2, "3:83600");
		
	}
	
	// add one test for redis get and for update
	@Test
	public void addUpdateSuggestionTest() {
		CacheService cacheService = new RedisService();
		Response response = cacheService.connect();
		assertEquals(response.ordinal(), 0);
		
		cacheService.updateSuggestion(new Suggestion("1", "2", "Accepted"));
		
		
		JedisPooled jedis = new JedisPooled("localhost", 6379);
		String value1 = jedis.lpop("1");
		
		assertNotNull(value1);
		assertEquals(value1, "2");
		
	}
	
	@Test
	public void getNextTimerEventTest() {
		CacheService cacheService = new RedisService();
		Response response = cacheService.connect();
		assertEquals(response.ordinal(), 0);
		JedisPooled jedis = new JedisPooled("localhost", 6379);
		jedis.rpush("-1", "2:3600");
		
		Company company = new Company("1");
		String result = (String) cacheService.getNextTimerEvent(company);
		
		cacheService.clearCache();
		assertNotNull(result);
		assertEquals(result, "2:3600");
	}
	
	@Test
	public void getCurrentSuggestionsTest() {
		CacheService cacheService = new RedisService();
		Response response = cacheService.connect();
		assertEquals(response.ordinal(), 0);
		JedisPooled jedis = new JedisPooled("localhost", 6379);
		jedis.rpush("+1", "2");
		jedis.rpush("+1", "3");
		
		jedis.rpush("1", "2");
		Company company = new Company("1");
		
		List<String> result = cacheService.getCurrentSuggestions(company);
		cacheService.clearCache();
		
		assertNotNull(result);
		assertEquals(result.size(), 1);
		assertEquals(result.get(0), "3");
		
	}
	
	@Test
	public void extractEmailTimeTest() {
		CacheService cacheService = new RedisService();
		String[] result = cacheService.extractEmailTime("3:3600");
		
		assertNotNull(result);
		assertEquals(result[0], "3");
		assertEquals(result[1], "3600");
	}
	
}
