package com.suggestion.service.service.datatransfer;

import java.util.LinkedList;
import java.util.List;

public class DataTransferResponse {
	List<Outcome> outcomes;
	List<String> payload;
	
	public DataTransferResponse() {
		this.outcomes = new LinkedList<>();
		this.payload = new LinkedList<>();
	}
	
	public void addOutcome(Outcome outcome) {
		this.outcomes.add(outcome);
	}
	
	public void addPayload(String msg) {
		this.payload.add(msg);
	}

	public List<Outcome> getOutcomes() {
		return outcomes;
	}

	public List<String> getPayload() {
		return payload;
	}

}
