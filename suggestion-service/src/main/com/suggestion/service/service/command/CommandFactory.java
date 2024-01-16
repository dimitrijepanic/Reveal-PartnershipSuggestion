package com.suggestion.service.service.command;

import java.util.List;

import com.suggestion.service.domain.Company;
import com.suggestion.service.domain.Email;
import com.suggestion.service.domain.Suggestion;
import com.suggestion.service.domain.TimerEvent;
import com.suggestion.service.service.datatransfer.DataTransferEmail;

public class CommandFactory {
	
	// we can just do a switch based on it to clean it up, so we actually have a factory
	

	public SimilarCompaniesCommand buildSimilarCompaniesCommand(Company company) {
		return new SimilarCompaniesCommand(company.getIndustry(),company.getCountry());
	}
	
	// add STATUS
	public InsertSuggestionsCommand buildInsertSuggestionsCommand(Company company) {
		return new InsertSuggestionsCommand(company.getCompanyId(),company.getSuggestions());
	}


	public TimerEventsCommand buildTimerEventsCommand() {
		return new TimerEventsCommand();
	}


	public TimerCommand buildTimerCommand(Company company, TimerEvent timerEvent) {
		return new TimerCommand(company.getCompanyId(), timerEvent.getEmailType(), timerEvent.getTimestamp());
	}

	public UpdateSuggestionStatusCommand buildUpdateSuggestionStatusCommand(Suggestion suggestion) {
		return new UpdateSuggestionStatusCommand(suggestion.getCompanyId(), suggestion.getSuggestionId(), suggestion.getStatus());
	}

	public SendEmailCommand buildSendEmailCommand(Email email) {
		return new SendEmailCommand(email.getRecipient(), email.getContent(), email.getTitle());
	}

	public GetEmailCommand buildGetEmailCommand(TimerEvent tm, Company company) {
		return new GetEmailCommand(company.getCompanyId(), tm.getEmailType(), company.getSuggestions());
	}
	
	
}
