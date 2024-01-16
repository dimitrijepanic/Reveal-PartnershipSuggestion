package com.suggestion.service.service.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.suggestion.service.adapter.CompanyAdapter;
import com.suggestion.service.adapter.GrowthAdapter;
import com.suggestion.service.adapter.PersistenceAdapter;
import com.suggestion.service.adapter.TimerAdapter;
import com.suggestion.service.domain.TimerEvent;
import com.suggestion.service.service.RedisService;
import com.suggestion.service.service.SuggestionGeneratingService;
import com.suggestion.service.service.SuggestionGeneratingServiceImpl;
import com.suggestion.service.service.TimerStartServiceImpl;
import com.suggestion.service.service.command.CommandFactory;
import com.suggestion.service.service.command.CompanyCreatedCommand;
import com.suggestion.service.service.command.InsertSuggestionsCommand;
import com.suggestion.service.service.command.SimilarCompaniesCommand;
import com.suggestion.service.service.command.TimerCommand;
import com.suggestion.service.service.command.TimerEventsCommand;
import com.suggestion.service.service.datatransfer.DataTransferCompanies;
import com.suggestion.service.service.datatransfer.DataTransferResponse;
import com.suggestion.service.service.datatransfer.DataTransferResponse;
import com.suggestion.service.service.datatransfer.DataTransferTimerEvent;
import com.suggestion.service.service.datatransfer.DataTransferTimerEventList;
import com.suggestion.service.service.datatransfer.Outcome;

public class SuggestionServiceTest {

	// dodaj test za redis
	// odaj test za persistance
	// dodaj test za build company
	// dodaj test za celu funkcionalnost koja radi
	@Test
	public void TestCompleteService() {
//		List<String> companies = new ArrayList<>();
//		companies.add("1");
//		companies.add("2");
//		
//		CompanyCreatedCommand companyCreatedCommand = new CompanyCreatedCommand("3", "Reveal", "Software Engineering", "France");
//		DataTransferTimerEvent timerEvent = new DataTransferTimerEvent(1, 3600);
//		List<DataTransferTimerEvent> timerEventList = new ArrayList<>();
//		timerEventList.add(timerEvent);
//		DataTransferTimerEventList timerEvents = new DataTransferTimerEventList(timerEventList);
//		DataTransferCompanies dataTransferCompanies = new DataTransferCompanies(companies);
//		
//		CompanyAdapter companyAdapterMock = (CompanyAdapter) mock(CompanyAdapter.class);
//		GrowthAdapter growthAdapterMock = (GrowthAdapter) mock(GrowthAdapter.class);
//		TimerAdapter timerAdapterMock = (TimerAdapter) mock(TimerAdapter.class);
//		
//		when(companyAdapterMock.requestSimilarCompanies(any(SimilarCompaniesCommand.class))).thenReturn(dataTransferCompanies);
//		when(growthAdapterMock.getTimerEvents(any(TimerEventsCommand.class))).thenReturn(timerEvents);
//		when(timerAdapterMock.setTimer(any(TimerCommand.class))).thenReturn(new DataTransferResponse(Outcome.SUCCESS));
//		
//		SuggestionGeneratingService service = new SuggestionGeneratingServiceImpl(
//				companyAdapterMock,
//				growthAdapterMock,
//				new PersistenceAdapter(),
//				timerAdapterMock,
//				new CommandFactory());
//		
//		
//		DataTransferResult result = service.companyCreated(companyCreatedCommand);
//		
//		assertEquals(result.getSize(), 2);
//		assertEquals(result.getCompanyId(), "3");
//		assertEquals(result.getOutcome(), Outcome.SUCCESS);
//		assertEquals(result.getOutcome2(), Outcome.SUCCESS);
	}
	
	@Test
	public void SuggestionServiceNoSuggestionsReceivedTest() {
		List<String> companies = new ArrayList<>();		
		CompanyCreatedCommand companyCreatedCommand = new CompanyCreatedCommand("3", "Reveal", "Software Engineering", "France");
		DataTransferCompanies dataTransferCompanies = new DataTransferCompanies(companies);
		CompanyAdapter companyAdapterMock = (CompanyAdapter) mock(CompanyAdapter.class);
		
		when(companyAdapterMock.requestSimilarCompanies(any(SimilarCompaniesCommand.class))).thenReturn(dataTransferCompanies);
//		
		SuggestionGeneratingService service = new SuggestionGeneratingServiceImpl(
				companyAdapterMock,
				new GrowthAdapter(),
				new PersistenceAdapter(),
				new TimerAdapter(),
				new CommandFactory(),
				new RedisService(),
				new TimerStartServiceImpl(null, null, null)
				);
		
		
		DataTransferResponse result = service.companyCreated(companyCreatedCommand);
		
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(0), Outcome.NO_DATA);

	}
	
	@Test
	public void SuggestionServiceSuggestionsReceivedFailTest() {
		List<String> companies = null;	
		CompanyCreatedCommand companyCreatedCommand = new CompanyCreatedCommand("3", "Reveal", "Software Engineering", "France");
		DataTransferCompanies dataTransferCompanies = new DataTransferCompanies(companies);
		CompanyAdapter companyAdapterMock = (CompanyAdapter) mock(CompanyAdapter.class);
		
		
		when(companyAdapterMock.requestSimilarCompanies(any(SimilarCompaniesCommand.class))).thenReturn(dataTransferCompanies);	
		SuggestionGeneratingService service = new SuggestionGeneratingServiceImpl(
				companyAdapterMock,
				new GrowthAdapter(),
				new PersistenceAdapter(),
				new TimerAdapter(),
				new CommandFactory(),
				new RedisService(),
				new TimerStartServiceImpl(null, null, null)
				);
		
		
		DataTransferResponse result = service.companyCreated(companyCreatedCommand);
		
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(0), Outcome.FAIL);

	}
	
	@Test
	public void SuggestionServiceSuggestionsPersistFailTest() {
		List<String> companies = new ArrayList<>();	
		CompanyCreatedCommand companyCreatedCommand = new CompanyCreatedCommand("3", "Reveal", "Software Engineering", "France");
		DataTransferCompanies dataTransferCompanies = new DataTransferCompanies(companies);
		CompanyAdapter companyAdapterMock = (CompanyAdapter) mock(CompanyAdapter.class);
		PersistenceAdapter persistenceAdapterMock = (PersistenceAdapter) mock(PersistenceAdapter.class);
		DataTransferResponse response = new DataTransferResponse();
		
		response.addOutcome(Outcome.FAIL);
		response.addPayload("Failed to connect");
		companies.add("1");
		companies.add("2");
		when(companyAdapterMock.requestSimilarCompanies(any(SimilarCompaniesCommand.class))).thenReturn(dataTransferCompanies);	
		when(persistenceAdapterMock.saveCompanySuggestions(any(InsertSuggestionsCommand.class))).thenReturn(response);	
		
		SuggestionGeneratingService service = new SuggestionGeneratingServiceImpl(
				companyAdapterMock,
				new GrowthAdapter(),
				persistenceAdapterMock,
				new TimerAdapter(),
				new CommandFactory(),
				new RedisService(),
				new TimerStartServiceImpl(null, null, null)
				);
		
		
		DataTransferResponse result = service.companyCreated(companyCreatedCommand);
		
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(1), Outcome.FAIL);
	}
	
	@Test
	public void SuggestionServiceTimerEventsGenerationFailTest() {
		List<String> companies = new ArrayList<>();	
		CompanyCreatedCommand companyCreatedCommand = new CompanyCreatedCommand("3", "Reveal", "Software Engineering", "France");
		DataTransferCompanies dataTransferCompanies = new DataTransferCompanies(companies);
		DataTransferTimerEvent timerEvent1 = new DataTransferTimerEvent(1, 3600);
		DataTransferTimerEvent timerEvent2 = new DataTransferTimerEvent(2, 83600);
		List<DataTransferTimerEvent> timerEventList = null;
//		timerEventList.add(timerEvent1);
//		timerEventList.add(timerEvent2);
		DataTransferTimerEventList timerEvents = new DataTransferTimerEventList(timerEventList);
		
		CompanyAdapter companyAdapterMock = (CompanyAdapter) mock(CompanyAdapter.class);
		PersistenceAdapter persistenceAdapterMock = (PersistenceAdapter) mock(PersistenceAdapter.class);
		GrowthAdapter growthAdapterMock = (GrowthAdapter) mock(GrowthAdapter.class);
		
		DataTransferResponse response = new DataTransferResponse();
		
		response.addOutcome(Outcome.SUCCESS);
		response.addPayload("Successfully persisted Data");
		companies.add("1");
		companies.add("2");
		
		when(companyAdapterMock.requestSimilarCompanies(any(SimilarCompaniesCommand.class))).thenReturn(dataTransferCompanies);	
		when(persistenceAdapterMock.saveCompanySuggestions(any(InsertSuggestionsCommand.class))).thenReturn(response);	
		when(growthAdapterMock.getTimerEvents(any(TimerEventsCommand.class))).thenReturn(timerEvents);
		
		SuggestionGeneratingService service = new SuggestionGeneratingServiceImpl(
				companyAdapterMock,
				growthAdapterMock,
				persistenceAdapterMock,
				new TimerAdapter(),
				new CommandFactory(),
				new RedisService(),
				new TimerStartServiceImpl(null, null, null)
				);
		
		
		DataTransferResponse result = service.companyCreated(companyCreatedCommand);
		
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(0), Outcome.SUCCESS);
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(1), Outcome.SUCCESS);
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(2), Outcome.FAIL);
	}
	
	@Test
	public void SuggestionServiceNoTimerEventsReceivedTest() {
		List<String> companies = new ArrayList<>();	
		CompanyCreatedCommand companyCreatedCommand = new CompanyCreatedCommand("3", "Reveal", "Software Engineering", "France");
		DataTransferCompanies dataTransferCompanies = new DataTransferCompanies(companies);
		DataTransferTimerEvent timerEvent1 = new DataTransferTimerEvent(1, 3600);
		DataTransferTimerEvent timerEvent2 = new DataTransferTimerEvent(2, 83600);
		List<DataTransferTimerEvent> timerEventList = new ArrayList<>();
//		timerEventList.add(timerEvent1);
//		timerEventList.add(timerEvent2);
		DataTransferTimerEventList timerEvents = new DataTransferTimerEventList(timerEventList);
		
		CompanyAdapter companyAdapterMock = (CompanyAdapter) mock(CompanyAdapter.class);
		PersistenceAdapter persistenceAdapterMock = (PersistenceAdapter) mock(PersistenceAdapter.class);
		GrowthAdapter growthAdapterMock = (GrowthAdapter) mock(GrowthAdapter.class);
		
		DataTransferResponse response = new DataTransferResponse();
		
		response.addOutcome(Outcome.SUCCESS);
		response.addPayload("Successfully persisted Data");
		companies.add("1");
		companies.add("2");
		
		when(companyAdapterMock.requestSimilarCompanies(any(SimilarCompaniesCommand.class))).thenReturn(dataTransferCompanies);	
		when(persistenceAdapterMock.saveCompanySuggestions(any(InsertSuggestionsCommand.class))).thenReturn(response);	
		when(growthAdapterMock.getTimerEvents(any(TimerEventsCommand.class))).thenReturn(timerEvents);
		
		SuggestionGeneratingService service = new SuggestionGeneratingServiceImpl(
				companyAdapterMock,
				growthAdapterMock,
				persistenceAdapterMock,
				new TimerAdapter(),
				new CommandFactory(),
				new RedisService(),
				new TimerStartServiceImpl(null, null, null)
				);
		
		
		DataTransferResponse result = service.companyCreated(companyCreatedCommand);
		
		assertNotNull(result);
		assertEquals(result.getOutcomes().get(0), Outcome.SUCCESS);
		assertEquals(result.getOutcomes().get(1), Outcome.SUCCESS);
		assertEquals(result.getOutcomes().get(2), Outcome.NO_DATA);
	}
	
	
	
	
}
