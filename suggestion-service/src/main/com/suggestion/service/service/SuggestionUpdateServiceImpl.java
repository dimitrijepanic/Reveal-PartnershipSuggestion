package com.suggestion.service.service;

import com.suggestion.service.adapter.PersistenceAdapter;
import com.suggestion.service.adapter.WebAdapter;
import com.suggestion.service.domain.Response;
import com.suggestion.service.domain.Suggestion;
import com.suggestion.service.service.command.CommandFactory;
import com.suggestion.service.service.command.UpdateSuggestionCommand;
import com.suggestion.service.service.datatransfer.DataTransferResponse;
import com.suggestion.service.service.datatransfer.Outcome;

import redis.clients.jedis.JedisPooled;

public class SuggestionUpdateServiceImpl implements SuggestionUpdateService {

	private PersistenceAdapter persistenceAdapter;
	private CommandFactory commandFactory;
	private CacheService cacheService;
	
	public SuggestionUpdateServiceImpl(PersistenceAdapter persistenceAdapter,
			CommandFactory commandFactory,
			CacheService cacheService) {
		super();
		this.persistenceAdapter = persistenceAdapter;
		this.commandFactory = commandFactory;
		this.cacheService = cacheService;
	}

	/*
	 * 
	 */
	@Override
	public DataTransferResponse updateSuggestion(UpdateSuggestionCommand command) {
		Suggestion suggestion = buildSuggestion(command);
		DataTransferResponse response = new DataTransferResponse();
		
		Response cacheResponse =  cacheUpdate(suggestion);
		
		ResponsePayloadUtility.addGeneralResponse(response, cacheResponse, 6);
		if(cacheResponse.equals(Response.FAIL)) return response;
		
		DataTransferResponse responseDB = persistenceAdapter.updateSuggestionStatus(commandFactory.buildUpdateSuggestionStatusCommand(suggestion));  
		
		ResponsePayloadUtility.addDataTransferResponse(response, responseDB);

		return response;
	}
	
	private Suggestion buildSuggestion(UpdateSuggestionCommand command) {
		return new Suggestion(command.getCompanyId(), command.getSuggestionId(), command.getStatus());
	}

	private Response cacheUpdate(Suggestion suggestion) {
		Response r = cacheService.connect();
		if(r.equals(Response.FAIL)) return r;
		return cacheService.updateSuggestion(suggestion);
	}

}
