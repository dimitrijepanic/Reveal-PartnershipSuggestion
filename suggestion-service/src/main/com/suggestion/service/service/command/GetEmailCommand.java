package com.suggestion.service.service.command;

import java.util.List;

public class GetEmailCommand {
	
	private String companyId;
	private int emailType;
	private List<String> suggestions;
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public int getEmailType() {
		return emailType;
	}
	public void setEmailType(int emailType) {
		this.emailType = emailType;
	}
	public List<String> getSuggestions() {
		return suggestions;
	}
	public void setSuggestions(List<String> suggestions) {
		this.suggestions = suggestions;
	}
	public GetEmailCommand(String companyId, int emailType, List<String> suggestions) {
		super();
		this.companyId = companyId;
		this.emailType = emailType;
		this.suggestions = suggestions;
	}
	
	

}
