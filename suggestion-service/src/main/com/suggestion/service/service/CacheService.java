package com.suggestion.service.service;

import java.util.List;

import com.suggestion.service.domain.Company;
import com.suggestion.service.domain.Response;
import com.suggestion.service.domain.Suggestion;
import com.suggestion.service.domain.TimerEvent;

public interface CacheService {
	public Response connect(String host, int port);
	public Response connect();
	public Response addCompanySuggestions(Company company);
	public Response addTimerEvents(Company company, List<TimerEvent> timerEvents);
	public Response updateSuggestion(Suggestion suggestion);
	public List<String> getCurrentSuggestions(Company company);
	public Object getNextTimerEvent(Company company);
	public String[] extractEmailTime(String value);
	public void clearCache();
}
