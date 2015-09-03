package com.dexpert.feecollection.main.fee;

public class CartDataBean {

	private Integer id;
	private String name;
	private String appId;
	private Double amount;
	private String dueString;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getDueString() {
		return dueString;
	}
	public void setDueString(String dueString) {
		this.dueString = dueString;
	}
	
	
}
