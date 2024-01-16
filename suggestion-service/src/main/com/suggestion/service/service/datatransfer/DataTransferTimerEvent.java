package com.suggestion.service.service.datatransfer;

public class DataTransferTimerEvent {
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
	public DataTransferTimerEvent(int emailType, long timestamp) {
		super();
		this.emailType = emailType;
		this.timestamp = timestamp;
	}
	
	
}
