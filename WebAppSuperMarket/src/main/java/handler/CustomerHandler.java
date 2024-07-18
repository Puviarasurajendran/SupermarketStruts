package handler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.crm.dao.SuperMarketSalesDAO;
import com.crm.model.Customer;
import com.google.gson.Gson;
public class CustomerHandler implements HandlerInterface {

	Customer customer = new Customer();

	List<Customer> customersList;

	private SuperMarketSalesDAO customersDAO = new SuperMarketSalesDAO();



	public CustomerHandler() {
		super();
	}

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
            List<Customer> customersList = customersDAO.getAllCustomers_v2();
            System.out.println("Super Market Billing Application CustomerList "+customersList);
            request.setAttribute("customersList", customersList);
            Gson gson = new Gson();
            String json = gson.toJson(customersList);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            response.getWriter().close();

        } catch (Exception e) {
            e.printStackTrace();

            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while fetching customers.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return "error";
        }
        return "success";
    }

    @Override
    public String handlePost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String firstName = request.getParameter("customer.firstName");
            String lastName = request.getParameter("customer.lastName");
            String email = request.getParameter("customer.email");
            String phone = request.getParameter("customer.phone");
            String address = request.getParameter("customer.address");

            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setAddress(address);

            System.out.println("Customer Details :"+customer);
            if (validationCheck(customer)) {
                customersDAO.addNewCustomer(customer);
                List<Customer> customersList = customersDAO.getAllCustomers_v1();
                request.setAttribute("customersList", customersList);
                request.getRequestDispatcher("/pages/customers.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid customer data.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while adding a customer.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return "ERROR";
        }
        return "success";
    }

	@Override
	public String handlePut(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@Override
	public String handleDelete(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	 public boolean validationCheck(Customer c) {
	    	if(c.getFirstName().length()>0&&c.getFirstName()!=null&&
	    			c.getLastName().length()>0&&c.getLastName()!=null &&
	    			c.getEmail().length()>0&&c.getEmail()!=null &&
	    			c.getPhone().length()>0&&c.getPhone()!=null) {
	    		return true;
	    	}
	    	return false;
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
