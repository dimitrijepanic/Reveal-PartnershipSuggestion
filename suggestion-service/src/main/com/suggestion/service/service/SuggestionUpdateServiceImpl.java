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

/*
 * expected this service to experience the highest load
 * for that reason the goal with the cache service and the 
 * actions performed but the update service is completely non-blocking
 */
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

	@Override
	public DataTransferResponse updateSuggestion(UpdateSuggestionCommand command) {
		Suggestion suggestion = buildSuggestion(command);
		DataTransferResponse response = new DataTransferResponse();
		
		// just append to the back of the list the update that had been done
		// code is pretty robust
		// code will still work no matter the number of updates
		// code is completely thread-safe (because of redis's nature)
		// if multiple updates are sent all of them will be saved to the 
		// cache, however the decision was this is ok -- for the latency
		Response cacheResponse =  cacheUpdate(suggestion);
		
		ResponsePayloadUtility.addGeneralResponse(response, cacheResponse, 6);
		if(cacheResponse.equals(Response.FAIL)) return response;
		
		// insert to the DB the update of the suggestion
		DataTransferResponse responseDB = persistenceAdapter.updateSuggestionStatus(commandFactory.buildUpdateSuggestionStatusCommand(suggestion));
		
		// if unsuccessful we can have garbage in the cache	
		// however the probability of that happening is low
		// so having one (value, key) is not such an issue as it will be cleaned up
		// afterwards
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
