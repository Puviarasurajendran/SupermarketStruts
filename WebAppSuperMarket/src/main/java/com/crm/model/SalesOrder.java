package com.crm.model;

import java.sql.Date;

public class SalesOrder {

    private int customerId;
    private String product;
    private int quantity;
    private String customerName;
    private String customerPhone;
    private double amount;
    private Date orderDate;
    private int productId;

    public SalesOrder() {}


	public int getCustomerId() {
		return customerId;
	}


	public String getProduct() {
		return product;
	}


	public void setProduct(String product) {
		this.product = product;
	}


	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public String getCustomerPhone() {
		return customerPhone;
	}


	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public Date getOrderDate() {
		return orderDate;
	}


	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}


	public int getProductId() {
		return productId;
	}


	public void setProductId(int productId) {
		this.productId = productId;
	}


	@Override
	public String toString() {
		return "SalesOrder [customerId=" + customerId + ", product=" + product + ", quantity=" + quantity
				+ ", customerName=" + customerName + ", customerPhone=" + customerPhone + ", amount=" + amount + "]";
	}




}

