package com.suggestion.service.service.datatransfer;

import java.util.List;

public class DataTransferCompanies {
	public List<String> suggestions;

	public List<String> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(List<String> suggestions) {
		this.suggestions = suggestions;
	}

	public DataTransferCompanies(List<String> suggestions) {
		super();
		this.suggestions = suggestions;
	}
	
}
