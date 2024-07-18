package com.crm.dao;

import java.util.List;

import com.crm.model.Customer;
import com.crm.model.Product;
import com.crm.model.SalesOrder;
import com.crm.model.Vendor;

public interface SuperMarketSalesInterface {

	List<Customer> getAllCustomers_v1();
	List<Customer> getAllCustomers_v2();
	void addNewCustomer(Customer customer);

	List<Product> getAllAvailableProducts();
	boolean saveSalesOrder_v1(SalesOrder order);

    List<SalesOrder> getAllSalesDeatils_v1();
    boolean saveInvoiceDetails_v1(int cId,Double totalAmount);

    void addProducts(Product product);
    void UpdateProductDetails(Product product);

    List<Vendor> getAllVendor();
    void addNewVendor(Vendor vendor);

    boolean updateCreditPoints_v1(int customerId, double totalAmount);
}
