package handler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.crm.dao.SuperMarketSalesDAO;
import com.crm.model.Vendor;
import com.google.gson.Gson;

public class VendorHandler implements HandlerInterface{


	private SuperMarketSalesDAO vendorDAO = new SuperMarketSalesDAO();
	List<Vendor> vendorList;

	@Override
	public String excute(HttpServletRequest request,HttpServletResponse response) {
		   String httpMethod = request.getMethod();

		   System.out.println("Request url Http Method :"+httpMethod);
	        switch (httpMethod) {
	            case "GET":
	                return handleGet(request, response);
	            case "POST":
	                return handlePost(request, response);
	            case "PUT":
	                return handlePut(request, response);
	            case "DELETE":
	                return handleDelete(request, response);
	            default:
	                throw new UnsupportedOperationException("HTTP method not supported: " + httpMethod);
	        }
	}


	@Override
	public String handleGet(HttpServletRequest request, HttpServletResponse response) {
		   try {
	            vendorList = vendorDAO.getAllVendor();
	            System.out.println("Super Market Billing Application vendorList "+vendorList);
	            request.setAttribute("vendorList", vendorList);

	            Gson gson = new Gson();
	            String json = gson.toJson(vendorList);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(json);
	            response.getWriter().close();

	            return "success";
	        } catch (Exception e) {
	            e.printStackTrace();

	            try {
	                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while fetching vendor");
	            } catch (IOException ioException) {
	                ioException.printStackTrace();
	            }
	            return "error";
	        }

	}

	@Override
	public String handlePost(HttpServletRequest request, HttpServletResponse response) {
		try {
		    String name = request.getParameter("vendor.name");
		    String contactName = request.getParameter("vendor.contactName");
		    String contactPhone = request.getParameter("vendor.contactPhone");
		    String address = request.getParameter("vendor.address");
		    String email = request.getParameter("vendor.vendor_email");

		    Vendor vendor = new Vendor();
		    vendor.setName(name);
		    vendor.setContactName(contactName);
		    vendor.setContactPhone(contactPhone);
		    vendor.setAddress(address);
		    vendor.setVendor_email(email);

		       System.out.println("Vendor Details: " + vendor);

		        vendorDAO.addNewVendor(vendor);
		        vendorList = vendorDAO.getAllVendor();
		        request.setAttribute("vendorList", vendorList);
		        request.getRequestDispatcher("/pages/vendor.jsp").forward(request, response);

		} catch (Exception e) {
		    e.printStackTrace();
		    try {
		        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while adding a vendor.");
		    } catch (IOException ioException) {
		        ioException.printStackTrace();
		    }
		    return "ERROR";
		}
		return "success";

	}
	@Override
	public String handlePut(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String handleDelete(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<Vendor> getVendorList() {
		return vendorList;
	}


	public void setVendorList(List<Vendor> vendorList) {
		this.vendorList = vendorList;
	}


	@Override
	public List getAction() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String putAction(Map<String, String> itemMap) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String postAction(Map<String, String> itemMap) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String deleteAction() {
		// TODO Auto-generated method stub
		return null;
	}


}
