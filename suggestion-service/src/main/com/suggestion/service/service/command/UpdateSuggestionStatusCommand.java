package com.suggestion.service.service.command;

public class UpdateSuggestionStatusCommand {
	private String companyId;
	private String suggestionId;
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
	public UpdateSuggestionStatusCommand(String companyId, String suggestionId, String status) {
		super();
		this.companyId = companyId;
		this.suggestionId = suggestionId;
		this.status = status;
	}
}
