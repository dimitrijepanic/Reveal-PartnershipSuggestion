package com.suggestion.service.service;

import java.util.List;

import com.suggestion.service.adapter.GrowthAdapter;
import com.suggestion.service.adapter.MailerAdapter;
import com.suggestion.service.domain.Company;
import com.suggestion.service.domain.Email;
import com.suggestion.service.domain.Response;
import com.suggestion.service.domain.TimerEvent;
import com.suggestion.service.service.command.CommandFactory;
import com.suggestion.service.service.command.TimerExpiredCommand;
import com.suggestion.service.service.datatransfer.DataTransferEmail;
import com.suggestion.service.service.datatransfer.DataTransferResponse;
import com.suggestion.service.service.datatransfer.Outcome;


public class TimerReceiverServiceImpl implements TimerReceiverService {

	private GrowthAdapter growthAdapter;
	private MailerAdapter mailerAdapter;
	private CommandFactory commandFactory;
	private CacheService cacheService;
	private TimerStartService timerStartService;

	public TimerReceiverServiceImpl(GrowthAdapter growthAdapter, MailerAdapter mailerAdapter,
			CommandFactory commandFactory, CacheService cacheService, TimerStartService timerStartService) {
		super();
		this.growthAdapter = growthAdapter;
		this.mailerAdapter = mailerAdapter;
		this.commandFactory = commandFactory;
		this.cacheService = cacheService;
		this.timerStartService = timerStartService;
	}

	@Override
	public DataTransferResponse timerExpired(TimerExpiredCommand command) {
		TimerEvent timerEvent = buildTimerEvent(command);
		Company company = new Company(command.getCompanyId());
		DataTransferResponse response = new DataTransferResponse();
		// collect the suggestions from the cache
		// collect the updates from the cache
		// compare them and use the difference
		List<String> suggestionIds = getCacheSuggestions(company);
		
		ResponsePayloadUtility.addResponseList(response, suggestionIds == null, suggestionIds == null ? -1 : suggestionIds.size(), company , 10);
		if(suggestionIds == null || suggestionIds.size() == 0) {
			return response;
		}
		
		company.setSuggestions(suggestionIds);
		
		// generate the email that needs to be sent
		DataTransferEmail dataTransferEmail = growthAdapter.generateEmail(commandFactory.buildGetEmailCommand(timerEvent, company));

		ResponsePayloadUtility.addDataTransferEmailResponse(response, dataTransferEmail);
		if(dataTransferEmail == null) return response;
		
		// actually tell the responsible service to send the email
		Email email = buildEmail(dataTransferEmail);
		DataTransferResponse mailResponse = mailerAdapter.sendEmail(commandFactory.buildSendEmailCommand(email));

		ResponsePayloadUtility.addDataTransferResponse(response, mailResponse);
		if(mailResponse == null || (!mailResponse.getOutcomes().get(0).equals(Outcome.SUCCESS))) {
			return response;
		}
		
		// call the service to set the timer
		// dependecy injection not the best solution!
		Response responseTimerStartService = timerStartService.startNextTimer(company);
		if(responseTimerStartService.equals(Response.SUCCESS)) {
			addSuggestionsToCache(company);
		}
		ResponsePayloadUtility.addGeneralResponse(response, responseTimerStartService, 8);
		
		return response;
	}
	
	private List<String> getCacheSuggestions(Company company){
		cacheService.connect();
		return cacheService.getCurrentSuggestions(company);
	}
	
	private Response addSuggestionsToCache(Company company){
		cacheService.connect();
		return cacheService.addCompanySuggestions(company);
	}
	
	private TimerEvent buildTimerEvent(TimerExpiredCommand command) {
		return new TimerEvent(command.getCompanyId(), command.getEmailType(), command.getTimestamp());
	}
	
	private Email buildEmail(DataTransferEmail dataTransferEmail){
		return new Email(dataTransferEmail.getRecipient(), dataTransferEmail.getContent(), dataTransferEmail.getTitle());
	}
}
