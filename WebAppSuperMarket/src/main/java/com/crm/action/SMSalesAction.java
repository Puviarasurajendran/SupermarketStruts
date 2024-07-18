package com.crm.action;

import java.util.List; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.crm.dao.SuperMarketSalesDAO;
import com.crm.model.Customer;
import com.crm.model.Product;
import com.crm.model.Vendor;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import handler.CustomerHandler;

public class SMSalesAction extends ActionSupport implements Preparable ,ServletRequestAware , ServletResponseAware {

	private Customer customer = new Customer();
	private Product product = new Product();
    private List<Customer> customersList;
    private List<Product> productList;
    private SuperMarketSalesDAO customersDAO = new SuperMarketSalesDAO();

    private List<Vendor> vendorsList;
    private Vendor vendor;

    HttpServletRequest request;
	HttpServletResponse response;

	CustomerHandler customerHandler= new CustomerHandler();

    @Override
	public void prepare() throws Exception {
		if (customer == null) {
            customer = new Customer();
        }
        if (product == null) {
            product = new Product();
        }
        if (vendor == null) {
        	vendor = new Vendor();
        }
	}

    @Override
	public String execute() {
    	System.out.println("Super Market Billing Application inside execute method");
        customersList = customersDAO.getAllCustomers_v1();
        customer = new Customer();
        String result= customerHandler.excute(request, response);
        customersList=(List<Customer>) request.getAttribute("customersList");
        System.out.println("Request details : "+customersList);
        return result;

    }

    public String addCustomer() {
    	System.out.println("Super Market Billing Application inside ADDCustomer method" + customer);
        customersDAO.addNewCustomer(customer);
        customersList = customersDAO.getAllCustomers_v1();
        customer = new Customer();
        return SUCCESS;
    }

    public String getAllCustomers() {
    	customersList = customersDAO.getAllCustomers_v2();
    	 System.out.println("Super Market Billing Application CustomerList for api call "+customersList);
    	return SUCCESS;
    }

    public String getAllProducts() {
    	productList = customersDAO.getAllAvailableProducts();
   	    System.out.println("Super Market Billing Application ProductList for api call "+productList);
    	return SUCCESS;
    }

    public String addProduct() {
    	System.out.println("Super Market Billing Application inside addProduct method" + product);
        customersDAO.addProducts(product);
        productList = customersDAO.getAllAvailableProducts();
        addActionMessage("Product added successfully.");
        return SUCCESS;
    }

    public String updateProduct() {
    	System.out.println("Super Market Billing Application inside updateProduct method" + product);
        customersDAO.UpdateProductDetails(product);
        productList = customersDAO.getAllAvailableProducts();
        addActionMessage("Product updated successfully.");
        return SUCCESS;
    }

    public String getAllVendors() {
    	vendorsList = customersDAO.getAllVendor();
   	    System.out.println("Super Market Billing Application vendorsList for api call "+vendorsList);
   	    vendor = new Vendor();
    	return SUCCESS;
    }

    public String addVendor() {
    	System.out.println("Super Market Billing Application inside addVendor method" + vendor);
        customersDAO.addNewVendor(vendor);
        vendorsList = customersDAO.getAllVendor();
        vendor = new Vendor();
        return SUCCESS;
    }

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Customer> getCustomersList() {
		return customersList;
	}

	public void setCustomersList(List<Customer> customersList) {
		this.customersList = customersList;
	}

	public SuperMarketSalesDAO getCustomersDAO() {
		return customersDAO;
	}

	public void setCustomersDAO(SuperMarketSalesDAO customersDAO) {
		this.customersDAO = customersDAO;
	}


	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}


	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public List<Vendor> getVendorsList() {
		return vendorsList;
	}

	public void setVendorsList(List<Vendor> vendorsList) {
		this.vendorsList = vendorsList;
	}

	@Override
	public void setServletRequest(HttpServletRequest req) {
		this.request=req;
	}


	@Override
	public void setServletResponse(HttpServletResponse res) {
		 this.response=res;
	}

}
