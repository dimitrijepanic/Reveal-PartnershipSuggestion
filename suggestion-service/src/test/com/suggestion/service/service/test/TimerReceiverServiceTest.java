package com.suggestion.service.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.suggestion.service.adapter.GrowthAdapter;
import com.suggestion.service.adapter.MailerAdapter;
import com.suggestion.service.adapter.PersistenceAdapter;
import com.suggestion.service.adapter.TimerAdapter;
import com.suggestion.service.domain.Response;
import com.suggestion.service.service.CacheService;
import com.suggestion.service.service.RedisService;
import com.suggestion.service.service.SuggestionUpdateService;
import com.suggestion.service.service.SuggestionUpdateServiceImpl;
import com.suggestion.service.service.TimerReceiverService;
import com.suggestion.service.service.TimerReceiverServiceImpl;
import com.suggestion.service.service.TimerStartServiceImpl;
import com.suggestion.service.service.command.CommandFactory;
import com.suggestion.service.service.command.GetEmailCommand;
import com.suggestion.service.service.command.SendEmailCommand;
import com.suggestion.service.service.command.TimerCommand;
import com.suggestion.service.service.command.TimerExpiredCommand;
import com.suggestion.service.service.command.UpdateSuggestionCommand;
import com.suggestion.service.service.command.UpdateSuggestionStatusCommand;
import com.suggestion.service.service.datatransfer.DataTransferEmail;
import com.suggestion.service.service.datatransfer.DataTransferResponse;
import com.suggestion.service.service.datatransfer.Outcome;

import redis.clients.jedis.JedisPooled;

public class TimerReceiverServiceTest {

	@Test
	public void TimerReceiverServiceNoDataTest() {
		TimerExpiredCommand timerExpiredCommand = new TimerExpiredCommand("1", 1, 36000);

		CacheService cacheService = new RedisService();
		
		TimerReceiverService service = new TimerReceiverServiceImpl(
				new GrowthAdapter(),
				new MailerAdapter(),
				new CommandFactory(),
				cacheService,
				new TimerStartServiceImpl(null, null, null)
				);
		
		DataTransferResponse result = service.timerExpired(timerExpiredCommand);
		cacheService.clearCache();
		
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(0), Outcome.NO_DATA);
	}
	
	@Test
	public void TimerReceiverServiceNoEmailReceivedTest() {
		TimerExpiredCommand timerExpiredCommand = new TimerExpiredCommand("1", 1, 36000);
		GrowthAdapter growthAdapterMock = (GrowthAdapter) mock(GrowthAdapter.class);
		CacheService cacheService = new RedisService();
		JedisPooled jedis = new JedisPooled("localhost", 6379);
//		DataTransferEmail dataTransferEmail = new DataTransferEmail("dp.dimitrije@gmail.com", "Follow us!", "Suggestion");
		
		jedis.lpush("+1", "2");
		when(growthAdapterMock.generateEmail(any(GetEmailCommand.class))).thenReturn(null);
		
		TimerReceiverService service = new TimerReceiverServiceImpl(
				growthAdapterMock,
				new MailerAdapter(),
				new CommandFactory(),
				cacheService,
				new TimerStartServiceImpl(null, null, null)
				);
		
		DataTransferResponse result = service.timerExpired(timerExpiredCommand);
		cacheService.clearCache();
		
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(1), Outcome.FAIL);
	}
	
	public void TimerReceiverServiceNoEmailSentTest() {
		TimerExpiredCommand timerExpiredCommand = new TimerExpiredCommand("1", 1, 36000);
		GrowthAdapter growthAdapterMock = (GrowthAdapter) mock(GrowthAdapter.class);
		MailerAdapter mailerAdapterMock = (MailerAdapter) mock(MailerAdapter.class);
		CacheService cacheService = new RedisService();
		JedisPooled jedis = new JedisPooled("localhost", 6379);
		DataTransferEmail dataTransferEmail = new DataTransferEmail("dp.dimitrije@gmail.com", "Follow us!", "Suggestion");
		
		jedis.lpush("+1", "2");
		when(growthAdapterMock.generateEmail(any(GetEmailCommand.class))).thenReturn(dataTransferEmail);
		when(mailerAdapterMock.sendEmail(any(SendEmailCommand.class))).thenReturn(null);
		
		TimerReceiverService service = new TimerReceiverServiceImpl(
				growthAdapterMock,
				mailerAdapterMock,
				new CommandFactory(),
				cacheService,
				new TimerStartServiceImpl(null, null, null)
				);
		
		DataTransferResponse result = service.timerExpired(timerExpiredCommand);
		cacheService.clearCache();
		
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(0), Outcome.SUCCESS);
		assertEquals(result.getOutcomes().get(1), Outcome.SUCCESS);
		assertEquals(result.getOutcomes().get(2), Outcome.FAIL);
	}
	
	@Test
	public void TimerReceiverServiceCompleteTest() {
		TimerExpiredCommand timerExpiredCommand = new TimerExpiredCommand("1", 1, 36000);
		GrowthAdapter growthAdapterMock = (GrowthAdapter) mock(GrowthAdapter.class);
		MailerAdapter mailerAdapterMock = (MailerAdapter) mock(MailerAdapter.class);
		CacheService cacheService = new RedisService();
		JedisPooled jedis = new JedisPooled("localhost", 6379);
		DataTransferEmail dataTransferEmail = new DataTransferEmail("dp.dimitrije@gmail.com", "Follow us!", "Suggestion");
		DataTransferResponse emailResponse = new DataTransferResponse();

		TimerAdapter timerAdapterMock = (TimerAdapter) mock(TimerAdapter.class);
		DataTransferResponse response = new DataTransferResponse();
		
		
		jedis.lpush("+1", "2");
		jedis.lpush("-1", "2:3600");
		emailResponse.addOutcome(Outcome.SUCCESS);
		emailResponse.addPayload("Successfull!");
		response.addOutcome(Outcome.SUCCESS);
		response.addPayload("Successfully set the timer");
		
		when(growthAdapterMock.generateEmail(any(GetEmailCommand.class))).thenReturn(dataTransferEmail);
		when(mailerAdapterMock.sendEmail(any(SendEmailCommand.class))).thenReturn(emailResponse);
		when(timerAdapterMock.setTimer(any(TimerCommand.class))).thenReturn(response);
		
		TimerReceiverService service = new TimerReceiverServiceImpl(
				growthAdapterMock,
				mailerAdapterMock,
				new CommandFactory(),
				cacheService,
				new TimerStartServiceImpl(timerAdapterMock, new CommandFactory(), cacheService)
				);
		
		DataTransferResponse result = service.timerExpired(timerExpiredCommand);
		cacheService.clearCache();
		
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(0), Outcome.SUCCESS);
		assertEquals(result.getOutcomes().get(1), Outcome.SUCCESS);
		assertEquals(result.getOutcomes().get(2), Outcome.SUCCESS);
		assertEquals(result.getOutcomes().get(3), Outcome.SUCCESS);
	}
}
