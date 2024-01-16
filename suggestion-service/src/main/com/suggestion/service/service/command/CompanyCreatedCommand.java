package com.suggestion.service.service.command;

public class CompanyCreatedCommand {
	private String companyId;
	private String name;
	private String industry;
	private String country;
	public CompanyCreatedCommand(String companyId, String name, String industry, String country) {
		super();
		this.companyId = companyId;
		this.name = name;
		this.industry = industry;
		this.country = country;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
