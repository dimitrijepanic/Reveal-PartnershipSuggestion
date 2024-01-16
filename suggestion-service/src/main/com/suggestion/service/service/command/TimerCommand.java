package com.suggestion.service.service.command;

public class TimerCommand {
	private String companyId;
	private int emailType;
	private long timestamp;
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
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public TimerCommand(String companyId, int emailType, long timestamp) {
		super();
		this.companyId = companyId;
		this.emailType = emailType;
		this.timestamp = timestamp;
	}
	
	
}
