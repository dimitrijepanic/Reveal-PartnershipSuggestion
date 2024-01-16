package com.suggestion.service.service.command;

import java.util.List;

public class InsertSuggestionsCommand {
	private String companyId;
	private List<String> suggestionIds;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public List<String> getSuggestionIds() {
		return suggestionIds;
	}
	public void setSuggestionIds(List<String> suggestionIds) {
		this.suggestionIds = suggestionIds;
	}
	public InsertSuggestionsCommand(String companyId, List<String> list) {
		super();
		this.companyId = companyId;
		this.suggestionIds = list;
	}
	
	
	
}
