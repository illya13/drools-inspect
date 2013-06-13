package com.github.illya13.drools.inspect;

public class TradeStatus {
	private String id;
	private TradeDate valueDate;
	private Status status;
	private Trade trade;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TradeDate getValueDate() {
		return valueDate;
	}
	public void setValueDate(TradeDate valueDate) {
		this.valueDate = valueDate;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Trade getTrade() {
		return trade;
	}
	public void setTrade(Trade trade) {
		this.trade = trade;
	}

	@Override
	public String toString() {
		return "TradeStatus [status=" + status + "]";
	}
}
