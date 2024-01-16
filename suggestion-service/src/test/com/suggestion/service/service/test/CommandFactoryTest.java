package com.suggestion.service.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.suggestion.service.domain.Company;
import com.suggestion.service.domain.TimerEvent;
import com.suggestion.service.service.command.CommandFactory;
import com.suggestion.service.service.command.InsertSuggestionsCommand;
import com.suggestion.service.service.command.SimilarCompaniesCommand;
import com.suggestion.service.service.command.TimerCommand;
import com.suggestion.service.service.command.TimerEventsCommand;

public class CommandFactoryTest {

	@Test
	public void buildSimilarCompaniesCommandTest() {
		Company company = createCompany();
		CommandFactory commandFactory = new CommandFactory();
		
		SimilarCompaniesCommand cmd = commandFactory.buildSimilarCompaniesCommand(company);
		
		assertEquals(cmd.getCountry(), "England");
		assertEquals(cmd.getIndustry(), "Business");
	}
	
	@Test
	public void buildInsertSuggestionsCommandTest() {
		Company company = createCompany();
		CommandFactory commandFactory = new CommandFactory();
		
		InsertSuggestionsCommand cmd = commandFactory.buildInsertSuggestionsCommand(company);
		
		assertEquals(cmd.getCompanyId(), "1");
		assertEquals(cmd.getSuggestionIds().size(), 2);
		assertEquals(cmd.getSuggestionIds().get(0), "2");
	}
	
	@Test
	public void buildTimerCommandTest() {
		Company company = createCompany();
		TimerEvent timerEvent = new TimerEvent(1, 3600);
		CommandFactory commandFactory = new CommandFactory();
		
		TimerCommand cmd = commandFactory.buildTimerCommand(company, timerEvent);
		
		assertEquals(cmd.getCompanyId(), "1");
		assertEquals(cmd.getEmailType(), 1);
		assertEquals(cmd.getTimestamp(), 3600);
	}
	
	@Test
	public void buildTimerEventsCommandTest() {
		CommandFactory commandFactory = new CommandFactory();
		
		TimerEventsCommand cmd = commandFactory.buildTimerEventsCommand();
		
		assertNotEquals(cmd, null);
	}
	
	private Company createCompany() {
		Company company = new Company("1", "Business", "England", "Meta");
		List<String> suggestions = new ArrayList<>();
		suggestions.add("2");
		suggestions.add("3");
		
		company.setSuggestions(suggestions);
		return company;
	}
}
