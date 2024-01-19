package com.suggestion.service.domain;

import java.util.ArrayList;
import java.util.List;
/*
 * Domain Object used ONLY in the Domain Logic and/or communication
 * between different services
 * Has to be transformed into a Command before sending to the adapter
 * Used to represent a Company Entity
 */
public class Company {
	
	private String companyId;
	private String name;
	private String industry;
	private String country;
	private List<String> suggestions;
	
	
	public Company(String companyId, String industry, String country, String name) {
		super();
		this.companyId = companyId;
		this.industry = industry;
		this.country = country;
		this.name = name;
		this.suggestions = new ArrayList<>();
	}
	
	public Company(String companyId) {
		super();
		this.companyId = companyId;
		this.suggestions = new ArrayList<>();
	}
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getSuggestions() {
		return suggestions;
	}
	public void setSuggestions(List<String> suggestions) {
		this.suggestions = suggestions;
	}
	
}
