package com.suggestion.service.service;

import com.suggestion.service.domain.Company;
import com.suggestion.service.domain.Response;
import com.suggestion.service.service.datatransfer.DataTransferEmail;
import com.suggestion.service.service.datatransfer.DataTransferResponse;
import com.suggestion.service.service.datatransfer.Outcome;

/*
 * Utility Class used to add action and command results to the DataTransfer
 * that will be sent to the Adapter
 */
public class ResponsePayloadUtility {
	
	public static String[] resultMessages = {
			"Successfully generated suggestions for company ",
			"Couldn't fetch suggestions for company ",
			"No suggestions exist for company ",
			"Successfully gathered the notification plan for company ",
			"Couldn't fetch the notification plan for company ",
			"No notification plan currently exists for company ",
			"Successfully put values in the cache",
			"Couldn't put values in the cache",
			"Successfully started a timer event",
			"Couldn't start a timer event",
			"Successfully gathered suggestions for company ",
			"Couldn't fetch suggestions for company ",
			"No suggestions exist for company ",
			"Unsuccessfull in generating an email",
			"Successfull in generating and email"
			
	};
	
	public static void addGeneralResponse(DataTransferResponse response, Response value, int id) {
		if(value !=  null && value.ordinal() == 0) {
			response.addOutcome(Outcome.SUCCESS);
			response.addPayload(ResponsePayloadUtility.resultMessages[id]);
		}
		else {
			response.addOutcome(Outcome.FAIL);
			response.addPayload(ResponsePayloadUtility.resultMessages[id + 1]);
		}
		
	}
	
	public static void addResponseList(DataTransferResponse response, boolean isNull, int size, Company company, int id) {
		String msg = ResponsePayloadUtility.resultMessages[id] + company.getCompanyId() + "; size = " + size;
		Outcome outcome = Outcome.SUCCESS;
		if(isNull) {
			outcome = Outcome.FAIL;
			msg = ResponsePayloadUtility.resultMessages[id + 1] + company.getCompanyId();
		} else if(size == 0) {
			outcome = Outcome.NO_DATA;
			msg = ResponsePayloadUtility.resultMessages[id + 2] + company.getCompanyId();
		}
		
		response.addOutcome(outcome);
		response.addPayload(msg);
	}
	
	public static void addDataTransferResponse(DataTransferResponse result, DataTransferResponse source) {
		if(source == null) {
			result.addOutcome(Outcome.FAIL);
			result.addPayload("Request Failed");
		} else {
			result.addOutcome(source.getOutcomes().get(0));
			result.addPayload(source.getPayload().get(0));
		}
		
	}
	
	public static void addDataTransferEmailResponse(DataTransferResponse response, DataTransferEmail dataTransferEmail) {
		if(dataTransferEmail == null) {
			response.addOutcome(Outcome.FAIL);
			response.addPayload(resultMessages[13]);
		}  else {
			response.addOutcome(Outcome.SUCCESS);
			response.addPayload(resultMessages[14]);
		}
	}
}
