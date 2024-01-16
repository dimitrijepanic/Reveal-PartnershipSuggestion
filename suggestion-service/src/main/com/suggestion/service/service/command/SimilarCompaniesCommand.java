package com.suggestion.service.service.command;

public class SimilarCompaniesCommand {
	private String industry;
	private String country;
	
	public SimilarCompaniesCommand(String industry, String country) {
		super();
		this.industry = industry;
		this.country = country;
	}
	
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
}
