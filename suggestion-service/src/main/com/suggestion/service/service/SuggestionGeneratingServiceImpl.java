package com.suggestion.service.service;
import java.util.ArrayList;
import java.util.List;

import com.suggestion.service.domain.Company;
import com.suggestion.service.domain.TimerEvent;
import com.suggestion.service.domain.Email;
import com.suggestion.service.domain.Response;
import com.suggestion.service.service.command.CommandFactory;
import com.suggestion.service.service.command.CompanyCreatedCommand;
import com.suggestion.service.service.datatransfer.DataTransferCompanies;
import com.suggestion.service.service.datatransfer.DataTransferResponse;
import com.suggestion.service.service.datatransfer.Outcome;
import com.suggestion.service.service.datatransfer.DataTransferTimerEvent;
import com.suggestion.service.service.datatransfer.DataTransferTimerEventList;

import redis.clients.jedis.JedisPooled;

import com.suggestion.service.adapter.CompanyAdapter;
import com.suggestion.service.adapter.GrowthAdapter;
import com.suggestion.service.adapter.PersistenceAdapter;
import com.suggestion.service.adapter.TimerAdapter;


public class SuggestionGeneratingServiceImpl implements SuggestionGeneratingService {
	
	private CompanyAdapter companyAdapter;
	private GrowthAdapter growthAdapter;
	private PersistenceAdapter persistenceAdapter;
	// the communication should go through a service registry but this is ok for now I think
	private CacheService cacheService;
	private TimerStartService timerStartService;
	CommandFactory commandFactory;
	
	public SuggestionGeneratingServiceImpl(
			CompanyAdapter companyAdapter,
			GrowthAdapter growthAdapter,
			PersistenceAdapter persistenceAdapter,
			CommandFactory commandFactory,
			CacheService cacheService,
			TimerStartService timerStartService) {
		this.companyAdapter = companyAdapter;
		this.growthAdapter = growthAdapter;
		this.persistenceAdapter = persistenceAdapter;
		this.commandFactory = commandFactory;
		this.cacheService = cacheService;
		this.timerStartService = timerStartService;
	}
	
	public DataTransferResponse companyCreated(CompanyCreatedCommand companyCreatedCommand) {
		Company company = buildCompany(companyCreatedCommand);
		DataTransferResponse response = new DataTransferResponse();
		// get the similar companies based on the company received through the command
		DataTransferCompanies dataTransferCompanies = companyAdapter.requestSimilarCompanies(commandFactory.buildSimilarCompaniesCommand(company)); 	
		
		List<String> suggestions = dataTransferCompanies.getSuggestions();
		
		ResponsePayloadUtility.addResponseList(response, suggestions == null, suggestions != null ? suggestions.size() : -1 ,company, 0);
		if(suggestions == null || suggestions.size() == 0) {
			return response;
		} 
		
		company.setSuggestions(dataTransferCompanies.getSuggestions());
		// save to DB the similar companies 
		// pk(companyId, suggestionId), timestamp
		// we expect the default of the DB to be status = pending
		DataTransferResponse responseDB = persistenceAdapter.saveCompanySuggestions(commandFactory.buildInsertSuggestionsCommand(company));
		
		ResponsePayloadUtility.addDataTransferResponse(response, responseDB);
		if(responseDB == null || responseDB.getOutcomes().get(0) == Outcome.FAIL) {
			return response;
		} 
	
		// get the policy and plan for sending emails 
		DataTransferTimerEventList dataTransferList = growthAdapter.getTimerEvents(commandFactory.buildTimerEventsCommand());
		List<TimerEvent> timerEvents = extractTimerEvents(dataTransferList.getDataTransferTimerEvents());
		ResponsePayloadUtility.addResponseList(response, timerEvents == null, timerEvents != null ? timerEvents.size() : -1, company, 3);
		if(timerEvents == null || timerEvents.size() == 0 ) {
			return response;
		}

		// add the timer events to cache (in reverse order)
		// add the suggestion list to the cache
		Response responseCache = addToCache(company, timerEvents);
		
		ResponsePayloadUtility.addGeneralResponse(response, responseCache, 6);
		if(!responseCache.equals(Response.SUCCESS)) return response;
		
		// call the timer service to start the timer
		// would be better if there existed a service registry
		Response responseTimerStartService = timerStartService.startNextTimer(company);
		ResponsePayloadUtility.addGeneralResponse(response, responseTimerStartService, 8);
		
		return response;
	}
	
	private Company buildCompany(CompanyCreatedCommand companyCreatedCommand) {
		return new Company(
				companyCreatedCommand.getCompanyId(),
				companyCreatedCommand.getIndustry(),
				companyCreatedCommand.getCountry(),
				companyCreatedCommand.getName());
	}
	
	private List<TimerEvent> extractTimerEvents(List<DataTransferTimerEvent> dataTransferTimerList) {
		if(dataTransferTimerList == null) return null;
		
		List<TimerEvent> timerEvents = new ArrayList<>();
		
		for(DataTransferTimerEvent dt: dataTransferTimerList) {
			timerEvents.add(new TimerEvent(dt.getEmailType(), dt.getTimestamp()));
		}
		
		return timerEvents;
	}
	
	// change this to not be ordinal
	private Response addToCache(Company company, List<TimerEvent> timerEvents) {
		// no singleton - anti-pattern
		Response r1 = cacheService.connect("localhost", 6379);
		if(!r1.equals(Response.SUCCESS)) return r1;
		
		Response r2 = cacheService.addCompanySuggestions(company);
		if(!r2.equals(Response.SUCCESS)) return r2;
		
		Response r3 = cacheService.addTimerEvents(company, timerEvents);
		return r3;
	}
	
	
}
