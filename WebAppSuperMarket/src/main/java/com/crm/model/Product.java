package com.crm.model;

public class Product {
    private int productId;
    private String productName;
    private String category;
    private double price;
    private int stock;
    private int vendorId;


    public Product() {}

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }


    public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", category=" + category
				+ ", price=" + price + ", stock=" + stock + ", vendorId=" + vendorId + "]";
	}


}

