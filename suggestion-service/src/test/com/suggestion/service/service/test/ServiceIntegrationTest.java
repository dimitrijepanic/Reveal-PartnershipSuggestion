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
import com.suggestion.service.adapter.MailerAdapter;
import com.suggestion.service.adapter.PersistenceAdapter;
import com.suggestion.service.adapter.TimerAdapter;
import com.suggestion.service.service.CacheService;
import com.suggestion.service.service.RedisService;
import com.suggestion.service.service.SuggestionGeneratingService;
import com.suggestion.service.service.SuggestionGeneratingServiceImpl;
import com.suggestion.service.service.SuggestionUpdateService;
import com.suggestion.service.service.SuggestionUpdateServiceImpl;
import com.suggestion.service.service.TimerReceiverService;
import com.suggestion.service.service.TimerReceiverServiceImpl;
import com.suggestion.service.service.TimerStartService;
import com.suggestion.service.service.TimerStartServiceImpl;
import com.suggestion.service.service.command.CommandFactory;
import com.suggestion.service.service.command.CompanyCreatedCommand;
import com.suggestion.service.service.command.GetEmailCommand;
import com.suggestion.service.service.command.InsertSuggestionsCommand;
import com.suggestion.service.service.command.SendEmailCommand;
import com.suggestion.service.service.command.SimilarCompaniesCommand;
import com.suggestion.service.service.command.TimerCommand;
import com.suggestion.service.service.command.TimerEventsCommand;
import com.suggestion.service.service.command.TimerExpiredCommand;
import com.suggestion.service.service.command.UpdateSuggestionCommand;
import com.suggestion.service.service.command.UpdateSuggestionStatusCommand;
import com.suggestion.service.service.datatransfer.DataTransferCompanies;
import com.suggestion.service.service.datatransfer.DataTransferEmail;
import com.suggestion.service.service.datatransfer.DataTransferResponse;
import com.suggestion.service.service.datatransfer.DataTransferTimerEvent;
import com.suggestion.service.service.datatransfer.DataTransferTimerEventList;
import com.suggestion.service.service.datatransfer.Outcome;

public class ServiceIntegrationTest {

	@Test
	public void ServiceIntegrationTestComplete() {
		List<String> companies = new ArrayList<>();	
		CompanyCreatedCommand companyCreatedCommand = new CompanyCreatedCommand("3", "Reveal", "Software Engineering", "France");
		DataTransferCompanies dataTransferCompanies = new DataTransferCompanies(companies);
		DataTransferTimerEvent timerEvent1 = new DataTransferTimerEvent(1, 3600);
		DataTransferTimerEvent timerEvent2 = new DataTransferTimerEvent(2, 83600);
		List<DataTransferTimerEvent> timerEventList = new ArrayList<>();
		CacheService cacheService = new RedisService();
		timerEventList.add(timerEvent1);
		timerEventList.add(timerEvent2);
		DataTransferTimerEventList timerEvents = new DataTransferTimerEventList(timerEventList);
		
		CompanyAdapter companyAdapterMock = (CompanyAdapter) mock(CompanyAdapter.class);
		PersistenceAdapter persistenceAdapterMock = (PersistenceAdapter) mock(PersistenceAdapter.class);
		GrowthAdapter growthAdapterMock = (GrowthAdapter) mock(GrowthAdapter.class);
		TimerAdapter timerAdapterMock = (TimerAdapter) mock(TimerAdapter.class);
		MailerAdapter mailerAdapterMock = (MailerAdapter) mock(MailerAdapter.class);
		
		DataTransferResponse responsePersistence = new DataTransferResponse();
		DataTransferResponse responseTimer = new DataTransferResponse();
		DataTransferEmail dataTransferEmail = new DataTransferEmail("dp.dimitrije@gmail.com", "Follow us!", "Suggestion");
		DataTransferResponse emailResponse = new DataTransferResponse();
		
		responsePersistence.addOutcome(Outcome.SUCCESS);
		responsePersistence.addPayload("Successfully set the timer");
		responseTimer.addOutcome(Outcome.SUCCESS);
		responseTimer.addPayload("Successfully persisted Data");
		emailResponse.addOutcome(Outcome.SUCCESS);
		emailResponse.addPayload("Successfull!");
		companies.add("1");
		companies.add("2");
		
		when(companyAdapterMock.requestSimilarCompanies(any(SimilarCompaniesCommand.class))).thenReturn(dataTransferCompanies);	
		when(persistenceAdapterMock.saveCompanySuggestions(any(InsertSuggestionsCommand.class))).thenReturn(responsePersistence);	
		when(persistenceAdapterMock.updateSuggestionStatus(any(UpdateSuggestionStatusCommand.class))).thenReturn(responsePersistence);	
		when(growthAdapterMock.getTimerEvents(any(TimerEventsCommand.class))).thenReturn(timerEvents);
		when(timerAdapterMock.setTimer(any(TimerCommand.class))).thenReturn(responseTimer);
		when(growthAdapterMock.generateEmail(any(GetEmailCommand.class))).thenReturn(dataTransferEmail);
		when(mailerAdapterMock.sendEmail(any(SendEmailCommand.class))).thenReturn(emailResponse);
		
		TimerStartService timerStartService = new TimerStartServiceImpl(
				timerAdapterMock,
				new CommandFactory(),
				cacheService);
		
		SuggestionGeneratingService serviceSuggestion = new SuggestionGeneratingServiceImpl(
				companyAdapterMock,
				growthAdapterMock,
				persistenceAdapterMock,
				new TimerAdapter(),
				new CommandFactory(),
				cacheService,
				timerStartService
				);
		
		
		DataTransferResponse result = serviceSuggestion.companyCreated(companyCreatedCommand);
		
//		cacheService.clearCache();
		
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(0), Outcome.SUCCESS);
		assertEquals(result.getOutcomes().get(1), Outcome.SUCCESS);
		assertEquals(result.getOutcomes().get(2), Outcome.SUCCESS);
		assertEquals(result.getOutcomes().get(3), Outcome.SUCCESS);
		assertEquals(result.getOutcomes().get(4), Outcome.SUCCESS);
		
		UpdateSuggestionCommand command = new UpdateSuggestionCommand("3", "1", "Accept");
		
		SuggestionUpdateService serviceUpdate = new SuggestionUpdateServiceImpl(
				persistenceAdapterMock,
				new CommandFactory(),
				cacheService
				);
		
		 result = serviceUpdate.updateSuggestion(command);
		 
//		 cacheService.clearCache();
		 assertNotNull(result);
		 assertEquals(result.getOutcomes().get(0), Outcome.SUCCESS);
		 assertEquals(result.getOutcomes().get(1), Outcome.SUCCESS);
		 
		 TimerExpiredCommand timerExpiredCommand = new TimerExpiredCommand("3", 1, 36000);
		 TimerReceiverService service = new TimerReceiverServiceImpl(
					growthAdapterMock,
					mailerAdapterMock,
					new CommandFactory(),
					cacheService,
					timerStartService
					);
			
		 result = service.timerExpired(timerExpiredCommand);
//		 cacheService.clearCache();
			
		 assertNotNull(result);
		 assertEquals(result.getOutcomes().get(0), Outcome.SUCCESS);
		 assertEquals(result.getOutcomes().get(1), Outcome.SUCCESS);
		 assertEquals(result.getOutcomes().get(2), Outcome.SUCCESS);
		 assertEquals(result.getOutcomes().get(3), Outcome.SUCCESS);
		 
		 command = new UpdateSuggestionCommand("3", "2", "Accept");
		
		 result = serviceUpdate.updateSuggestion(command);	 
		 
		 assertNotNull(result);
		 assertEquals(result.getOutcomes().get(0), Outcome.SUCCESS);
		 assertEquals(result.getOutcomes().get(1), Outcome.SUCCESS);
		 
		 timerExpiredCommand = new TimerExpiredCommand("3", 2, 83600);
		 result = service.timerExpired(timerExpiredCommand);
		 cacheService.clearCache();
		 
		 assertNotNull(result);
		 assertEquals(result.getOutcomes().get(0), Outcome.NO_DATA);
	}
}
