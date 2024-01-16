package com.suggestion.service.service;

import com.suggestion.service.domain.Company;
import com.suggestion.service.domain.Response;
import com.suggestion.service.domain.TimerEvent;

public interface TimerStartService {

	public Response startNextTimer(Company company);
}
