package com.crm.action;

import java.util.List;

import com.crm.dao.SuperMarketSalesDAO;
import com.crm.model.Invoice;
import com.crm.model.SalesOrder;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class SMInvoiceAction extends ActionSupport implements ModelDriven<Invoice> {

	    private Invoice invoice = new Invoice();
	    private SuperMarketSalesDAO customersDAO = new SuperMarketSalesDAO();
	    private String status;
	    List<SalesOrder> salesRecords;
        private int discount;

	    public String saveInvoiceDetails() {
	    	System.out.println("Super Market Billing Application SMInvoiceAction saveInvoiceDetails for api call "+invoice);
	    	List<SalesOrder> items = invoice.getItems();
	    	Double totalAmount=invoice.getTotalAmount();
	        int cId=0;
	        boolean allSaved = true;

	        for (SalesOrder item : items) {
	            boolean isSaved = customersDAO.saveSalesOrder_v1(item);
	            if (!isSaved) {
	            	System.out.println("Enter into saveInvoiceDetails "+item);
	                allSaved = false;
	                break;
	            }
	            cId=item.getCustomerId();
	        }
	        status = customersDAO.updateCreditPoints_v1(cId, 500)?"success":"error";

	        if (allSaved) {
	            status = "success";
	            System.out.println("Enter into saveInvoiceDetails if  before insert invoide details status"+status+"Cid :"+cId);
	            status = customersDAO.saveInvoiceDetails_v1(cId, totalAmount)?"success":"error";
	            System.out.println("Enter into saveInvoiceDetails if After insert invoide details status"+status+" Cid :"+cId);
	            status = customersDAO.updateCreditPoints_v1(cId, totalAmount)?"success":"error";
	            System.out.println("Enter into saveInvoiceDetails if After updateCreditPoints details status "+status+" Cid :"+cId);
	        } else {
	            status = "error";
	            System.out.println("Enter into saveInvoiceDetails else status"+status);
	        }
	        System.out.println("Enter into saveInvoiceDetails outer status"+status);
	        return SUCCESS;
	    }

	    public String getAllSalesRecords() {
	    	         salesRecords = customersDAO.getAllSalesDeatils_v1();
	    	    	 System.out.println("Super Market Billing Application salesRecordsList for api call "+salesRecords);
	    	    	return SUCCESS;
	    }

		@Override
		public Invoice getModel() {

			return invoice;
		}

		public Invoice getInvoice() {
			return invoice;
		}

		public void setInvoice(Invoice invoice) {
			this.invoice = invoice;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public List<SalesOrder> getSalesRecords() {
			return salesRecords;
		}

		public void setSalesRecords(List<SalesOrder> salesRecords) {
			this.salesRecords = salesRecords;
		}

		public int getDiscount() {
			return discount;
		}

		public void setDiscount(int discount) {
			this.discount = discount;
		}




}
