package com.suggestion.service.service.command;

public class UpdateSuggestionCommand {
	// do we need to also persist every action or just to update it
	private String companyId;
	private String suggestionId;
	// mozes status da prebacis u enum
	private String status;
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getSuggestionId() {
		return suggestionId;
	}
	public void setSuggestionId(String suggestionId) {
		this.suggestionId = suggestionId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public UpdateSuggestionCommand(String companyId, String suggestionId, String status) {
		super();
		this.companyId = companyId;
		this.suggestionId = suggestionId;
		this.status = status;
	}
	
	
}
