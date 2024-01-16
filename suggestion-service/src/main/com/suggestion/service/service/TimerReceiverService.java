package com.suggestion.service.service;

import com.suggestion.service.service.command.TimerExpiredCommand;
import com.suggestion.service.service.datatransfer.DataTransferResponse;

public interface TimerReceiverService {
	
	public DataTransferResponse timerExpired(TimerExpiredCommand command);
}
