package com.suggestion.service.adapter;

import com.suggestion.service.service.command.InsertSuggestionsCommand;
import com.suggestion.service.service.command.UpdateSuggestionStatusCommand;
import com.suggestion.service.service.datatransfer.DataTransferResponse;

public class PersistenceAdapter {

	// check the return signal
	public DataTransferResponse saveCompanySuggestions(InsertSuggestionsCommand buildInsertSuggestionsCommand) {
		return null;
	}
	
	public DataTransferResponse updateSuggestionStatus(UpdateSuggestionStatusCommand updateCommand) {
		return null;
	}

}
