package com.github.illya13.drools.inspect;

public class Trade {
	private String id;
	private Source source;
	private TradeDate valueDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
		this.source = source;
	}
	public TradeDate getValueDate() {
		return valueDate;
	}
	public void setValueDate(TradeDate valueDate) {
		this.valueDate = valueDate;
	}

	@Override
	public String toString() {
		return "Trade [id=" + id + ", source=" + source + ", valueDate="
				+ valueDate + "]";
	}
}
