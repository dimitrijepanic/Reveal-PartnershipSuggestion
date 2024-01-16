package com.suggestion.service.service.datatransfer;

import java.util.List;

public class DataTransferTimerEventList {
	private List<DataTransferTimerEvent> timerEvents;

	public List<DataTransferTimerEvent> getDataTransferTimerEvents() {
		return timerEvents;
	}

	public void setDataTransferTimerEvents(List<DataTransferTimerEvent> timerEvents) {
		this.timerEvents = timerEvents;
	}

	public DataTransferTimerEventList(List<DataTransferTimerEvent> timerEvents) {
		super();
		this.timerEvents = timerEvents;
	}
	
	
	
}
