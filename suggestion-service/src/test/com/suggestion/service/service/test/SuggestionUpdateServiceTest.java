package com.suggestion.service.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.suggestion.service.adapter.CompanyAdapter;
import com.suggestion.service.adapter.GrowthAdapter;
import com.suggestion.service.adapter.PersistenceAdapter;
import com.suggestion.service.adapter.TimerAdapter;
import com.suggestion.service.service.CacheService;
import com.suggestion.service.service.RedisService;
import com.suggestion.service.service.SuggestionGeneratingService;
import com.suggestion.service.service.SuggestionGeneratingServiceImpl;
import com.suggestion.service.service.SuggestionUpdateService;
import com.suggestion.service.service.SuggestionUpdateServiceImpl;
import com.suggestion.service.service.TimerStartServiceImpl;
import com.suggestion.service.service.command.CommandFactory;
import com.suggestion.service.service.command.CompanyCreatedCommand;
import com.suggestion.service.service.command.InsertSuggestionsCommand;
import com.suggestion.service.service.command.SimilarCompaniesCommand;
import com.suggestion.service.service.command.UpdateSuggestionCommand;
import com.suggestion.service.service.command.UpdateSuggestionStatusCommand;
import com.suggestion.service.service.datatransfer.DataTransferCompanies;
import com.suggestion.service.service.datatransfer.DataTransferResponse;
import com.suggestion.service.service.datatransfer.Outcome;

import redis.clients.jedis.JedisPooled;

public class SuggestionUpdateServiceTest {

	@Test
	public void SuggestionServiceSuggestionsPersistFailTest() {
		UpdateSuggestionCommand command = new UpdateSuggestionCommand("3", "1", "Accept");

		PersistenceAdapter persistenceAdapterMock = (PersistenceAdapter) mock(PersistenceAdapter.class);
		CacheService cacheService = new RedisService();
		DataTransferResponse response = new DataTransferResponse();
		
		response.addOutcome(Outcome.FAIL);
		response.addPayload("Failed to persist");
		when(persistenceAdapterMock.updateSuggestionStatus(any(UpdateSuggestionStatusCommand.class))).thenReturn(response);	
		
		SuggestionUpdateService service = new SuggestionUpdateServiceImpl(
				persistenceAdapterMock,
				new CommandFactory(),
				cacheService
				);
		
		DataTransferResponse result = service.updateSuggestion(command);
		cacheService.clearCache();
		
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(1), Outcome.FAIL);
	}
	
	// complete success
	@Test
	public void SuggestionServiceSuggestionsSuccessTest() {
		UpdateSuggestionCommand command = new UpdateSuggestionCommand("3", "1", "Accept");

		PersistenceAdapter persistenceAdapterMock = (PersistenceAdapter) mock(PersistenceAdapter.class);
		CacheService cacheService = new RedisService();
		DataTransferResponse response = new DataTransferResponse();
		
		response.addOutcome(Outcome.SUCCESS);
		response.addPayload("Successfully persisted to DB");
		when(persistenceAdapterMock.updateSuggestionStatus(any(UpdateSuggestionStatusCommand.class))).thenReturn(response);	
		
		SuggestionUpdateService service = new SuggestionUpdateServiceImpl(
				persistenceAdapterMock,
				new CommandFactory(),
				cacheService
				);
		
		DataTransferResponse result = service.updateSuggestion(command);
		
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(0), Outcome.SUCCESS);
		assertEquals(result.getOutcomes().get(1), Outcome.SUCCESS);
		
		JedisPooled jedis = new JedisPooled("localhost", 6379);
		String value = jedis.lpop("3");
		
		assertNotNull(value);
		assertEquals(value, "1");
		
		cacheService.clearCache();
	}
	
}
