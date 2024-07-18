package com.crm.springservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.crm.dao.SuperMarketSalesDAO;
import com.crm.model.Customer;

public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private SuperMarketSalesDAO customerDao; // Autowire your DAO class

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers_v1(); // Replace with your DAO method for fetching all customers
    }
    
}
