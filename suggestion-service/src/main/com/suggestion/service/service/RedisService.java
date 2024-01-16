package com.suggestion.service.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.suggestion.service.domain.Company;
import com.suggestion.service.domain.Response;
import com.suggestion.service.domain.Suggestion;
import com.suggestion.service.domain.TimerEvent;

import redis.clients.jedis.JedisPooled;

public class RedisService implements CacheService {

	private JedisPooled jedis;
	/*
	 * Add to the end of the list the current company suggestions
	 * @return Response(Fail or Success)
	 */
	@Override
	public Response addCompanySuggestions(Company company) {
		try {
			for(String str: company.getSuggestions()) {
				jedis.rpush("+" + company.getCompanyId(), str);
			}
		}catch (Exception e) {
			return Response.FAIL;
		}
		
		return Response.SUCCESS;
	}

	/*
	 * Add in reverse order the timer Events
	 * @return Response(Fail or Success)
	 */
	@Override
	public Response addTimerEvents(Company company, List<TimerEvent> timerEvents) {
		try {
			// do we need to sort them by time
			for(int i = timerEvents.size() - 1; i >= 0; i--) {
				TimerEvent tm = timerEvents.get(i);
				jedis.rpush("-" + company.getCompanyId(), tm.getEmailType() + ":" + tm.getTimestamp());
			}
		}catch (Exception e) {
			return Response.FAIL;
		}
		
		return Response.SUCCESS;
	}

	/*
	 * Add to the cache the updated Suggestion with its new Status
	 * @return Response(Fail or Success)
	 */
	@Override
	public Response updateSuggestion(Suggestion suggestion) {
		
		try {
			this.jedis.lpush(suggestion.getCompanyId(), suggestion.getSuggestionId());
		} catch(Exception exception) {
			return Response.FAIL;
		}
		
		return Response.SUCCESS;
	}

	/*
	 * connect to the database with the preffered host and port
	 * The service must be running on the machine/port
	 * @return Response(Fail or Success)
	 */
	@Override
	public Response connect(String host, int port) {
		try {
			this.jedis = new JedisPooled(host, port);
		} catch(Exception e) {
			return Response.FAIL;
		}
		return Response.SUCCESS;
	}

	@Override
	public Response connect() {
		try {
			this.jedis = new JedisPooled("localhost", 6379);
		} catch(Exception e) {
			return Response.FAIL;
		}
		return Response.SUCCESS;
	}

	/*
	 * get the next timer event from the sorted list
	 * @return Response(Fail or Success)
	 */
	@Override
	public Object getNextTimerEvent(Company company) {
		String value = "";
		try {
			value = this.jedis.rpop("-" + company.getCompanyId());
		} catch(Exception e) {
			return null;
		}
		return value;
	}

	/*
	 * empty redis completely
	 */
	@Override
	public void clearCache() {
		jedis.flushAll();
	}
	
	/*
	 * Collect the currently not updated suggestions
	 * @return Response(Fail or Success)
	 */
	@Override
	public List<String> getCurrentSuggestions(Company company) {
		try {
			Set<String> suggestionIds = new HashSet<>();

			String value = jedis.rpop("+" + company.getCompanyId());
			while(value != null && value != "") {
				suggestionIds.add(value);
				value = jedis.rpop("+" + company.getCompanyId());
			}
			
			value = jedis.rpop(company.getCompanyId());
			while(value != null && value != "") {
				suggestionIds.remove(value);
				value = jedis.rpop(company.getCompanyId());
			}

			return suggestionIds.stream().collect(Collectors.toList());
		} catch(Exception e) {
			return null;
		}
	}

}
