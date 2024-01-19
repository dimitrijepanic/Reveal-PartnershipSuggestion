package com.suggestion.service.domain;

/*
 * Domain Object used ONLY in the Domain Logic and/or communication
 * between different services
 * Has to be transformed into a Command before sending to the adapter
 * Used to represent a Timer Event Entity
 */
public class TimerEvent {
	private String companyId;
	private int emailType;
	private long timestamp;
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
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public TimerEvent(String companyId, int emailType, long timestamp) {
		super();
		this.companyId = companyId;
		this.emailType = emailType;
		this.timestamp = timestamp;
	}
	public TimerEvent(int emailType, long timestamp) {
		super();
		this.emailType = emailType;
		this.timestamp = timestamp;
	}
	
	
}
