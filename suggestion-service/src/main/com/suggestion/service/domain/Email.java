package com.suggestion.service.domain;

/*
 * Domain Object used ONLY in the Domain Logic and/or communication
 * between different services
 * Has to be transformed into a Command before sending to the adapter
 * Used to represent an Email Entity
 */
public class Email {
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
	public Email(String recipient, String content, String title) {
		super();
		this.recipient = recipient;
		this.content = content;
		this.title = title;
	}
	
}
