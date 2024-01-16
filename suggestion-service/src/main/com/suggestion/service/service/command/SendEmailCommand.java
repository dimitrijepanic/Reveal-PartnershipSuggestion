package com.suggestion.service.service.command;

public class SendEmailCommand {
	String recipient;
	String content;
	String title;
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public SendEmailCommand(String recipient, String content, String title) {
		super();
		this.recipient = recipient;
		this.content = content;
		this.title = title;
	}
	
}
