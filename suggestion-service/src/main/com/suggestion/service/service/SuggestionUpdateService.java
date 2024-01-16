package com.suggestion.service.service;

import com.suggestion.service.service.command.UpdateSuggestionCommand;
import com.suggestion.service.service.datatransfer.DataTransferResponse;

public interface SuggestionUpdateService {
	public DataTransferResponse updateSuggestion(UpdateSuggestionCommand command);
}
