package com.suggestion.service.service;

import com.suggestion.service.domain.Company;
import com.suggestion.service.service.command.CompanyCreatedCommand;
import com.suggestion.service.service.datatransfer.DataTransferResponse;

public interface SuggestionGeneratingService {

	public DataTransferResponse companyCreated(CompanyCreatedCommand companyCreatedCommand);
}
