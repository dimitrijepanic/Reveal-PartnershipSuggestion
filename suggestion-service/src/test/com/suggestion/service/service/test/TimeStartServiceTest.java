package com.suggestion.service.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.suggestion.service.adapter.TimerAdapter;
import com.suggestion.service.domain.Company;
import com.suggestion.service.domain.Response;
import com.suggestion.service.service.RedisService;
import com.suggestion.service.service.TimerStartService;
import com.suggestion.service.service.TimerStartServiceImpl;
import com.suggestion.service.service.command.CommandFactory;
import com.suggestion.service.service.command.TimerCommand;
import com.suggestion.service.service.datatransfer.DataTransferResponse;
import com.suggestion.service.service.datatransfer.Outcome;

import redis.clients.jedis.JedisPooled;

public class TimeStartServiceTest {

	@Test
	public void TimeStartServiceCompleteOneMailTest() {
		Company company = new Company("1", "Business", "France", "Meta");
		JedisPooled jedis = new JedisPooled("localhost", 6379);
		jedis.flushAll();
		TimerAdapter timerAdapterMock = (TimerAdapter) mock(TimerAdapter.class);
		DataTransferResponse response = new DataTransferResponse();
		response.addOutcome(Outcome.SUCCESS);
		response.addPayload("Successfully set the timer");
		
		jedis.rpush("-" + company.getCompanyId(), 1 + ":" + 3600);
//		jedis.rpush("-" + company.getCompanyId(), 4 + ":" + 83600);
		
		when(timerAdapterMock.setTimer(any(TimerCommand.class))).thenReturn(response);
		
		TimerStartService timerStartService = new TimerStartServiceImpl(
				timerAdapterMock,
				new CommandFactory(),
				new RedisService());
		
		Response result = timerStartService.startNextTimer(company);
		
		assertNotNull(result);
		assertEquals(result.ordinal(), 0);
	}
	
	@Test
	public void TimeStartServiceNoDataTest() {
		Company company = new Company("1", "Business", "France", "Meta");
		
		TimerStartService timerStartService = new TimerStartServiceImpl(
				new TimerAdapter(),
				new CommandFactory(),
				new RedisService());
		
		Response response = timerStartService.startNextTimer(company);
		
		assertNotNull(response);
		assertEquals(response.ordinal(), 2);
	}
	
	@Test
	public void TimeStartServiceFailTest() {
		Company company = new Company("1", "Business", "France", "Meta");
		
		JedisPooled jedis = new JedisPooled("localhost", 6379);
		jedis.flushAll();
		jedis.rpush("-" + company.getCompanyId(), 1 + ":" + 3600);
		
		TimerStartService timerStartService = new TimerStartServiceImpl(
				new TimerAdapter(),
				new CommandFactory(),
				new RedisService());
		
		Response response = timerStartService.startNextTimer(company);
		
		assertNotNull(response);
		assertEquals(response.ordinal(), 3);
	}
	
	@Test
	public void TimeStartServiceCompleteTwoMailTest() {
		Company company = new Company("1", "Business", "France", "Meta");
		JedisPooled jedis = new JedisPooled("localhost", 6379);
		jedis.flushAll();
		TimerAdapter timerAdapterMock = (TimerAdapter) mock(TimerAdapter.class);
		DataTransferResponse response = new DataTransferResponse();
		response.addOutcome(Outcome.SUCCESS);
		response.addPayload("Successfully set the timer");
		
		jedis.rpush("-" + company.getCompanyId(), 1 + ":" + 3600);
		jedis.rpush("-" + company.getCompanyId(), 4 + ":" + 83600);
		
		when(timerAdapterMock.setTimer(any(TimerCommand.class))).thenReturn(response);
		
		TimerStartService timerStartService = new TimerStartServiceImpl(
				timerAdapterMock,
				new CommandFactory(),
				new RedisService());
		
		Response result = timerStartService.startNextTimer(company);
		
		assertNotNull(result);
		assertEquals(result.ordinal(), 0);
		
		result = timerStartService.startNextTimer(company);
		
		assertNotNull(result);
		assertEquals(result.ordinal(), 0);
	}
}
