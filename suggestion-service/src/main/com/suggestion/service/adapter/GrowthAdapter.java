package com.suggestion.service.adapter;

import com.suggestion.service.service.command.GetEmailCommand;
import com.suggestion.service.service.command.TimerEventsCommand;
import com.suggestion.service.service.datatransfer.DataTransferEmail;
import com.suggestion.service.service.datatransfer.DataTransferTimerEventList;

public class GrowthAdapter {
	
	public DataTransferTimerEventList getTimerEvents(TimerEventsCommand timerEventsCommand) {
		return null;
	}

	public DataTransferEmail generateEmail(GetEmailCommand buildGetEmailCommand) {
		return null;
	}

}
