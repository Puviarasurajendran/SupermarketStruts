package com.crm.model;

import java.util.List;

public class Invoice {

	private List<SalesOrder> items;
    private double totalAmount;

	public List<SalesOrder> getItems() {
		return items;
	}
	public void setItems(List<SalesOrder> items) {
		this.items = items;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	@Override
	public String toString() {
		return "Invoice [items=" + items + ", totalAmount=" + totalAmount + "]";
	}

}

