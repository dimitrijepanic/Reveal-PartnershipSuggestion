package com.suggestion.service.service;

import com.suggestion.service.adapter.TimerAdapter;
import com.suggestion.service.domain.Company;
import com.suggestion.service.domain.Response;
import com.suggestion.service.domain.TimerEvent;
import com.suggestion.service.service.command.CommandFactory;
import com.suggestion.service.service.datatransfer.DataTransferResponse;
import com.suggestion.service.service.datatransfer.Outcome;

import redis.clients.jedis.JedisPooled;

public class TimerStartServiceImpl implements TimerStartService {

	private TimerAdapter timerAdapter;
	private CommandFactory commandFactory;
	private CacheService cacheService;

	public TimerStartServiceImpl(TimerAdapter timerAdapter, CommandFactory commandFactory, CacheService cacheService) {
		super();
		this.timerAdapter = timerAdapter;
		this.commandFactory = commandFactory;
		this.cacheService = cacheService;
	}

	@Override
	public Response startNextTimer(Company company) {
		cacheService.connect();
		String value = (String) cacheService.getNextTimerEvent(company);

		// if we have already done the last timer event 
		// just return from the method
		if(value == null || value == "") {
			return Response.NO_DATA;
		}
		
		// this can be a strategy for the way we extract information from the key
		// update it if you have time
		// however can be argued that it might be overcoding
		String[] values = cacheService.extractEmailTime(value);
		
		// actually set the ticks and start the timer 
		DataTransferResponse responseTimer = 
				timerAdapter.setTimer(
						commandFactory.buildTimerCommand(
								company,
								new TimerEvent(Integer.parseInt(values[0]),Long.parseLong(values[1]))
								));
		
		if(responseTimer == null || responseTimer.getOutcomes().get(0) == Outcome.FAIL) return Response.FAIL;
		return Response.SUCCESS;
	}
}
