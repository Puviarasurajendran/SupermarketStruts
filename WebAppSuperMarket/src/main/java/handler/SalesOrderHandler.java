package handler;

import java.io.BufferedReader;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.crm.dao.SuperMarketSalesDAO;
import com.crm.dao.SuperMarketSalesInterface;
import com.crm.model.Invoice;
import com.crm.model.SalesOrder;
import com.google.gson.Gson;

import proxy.CustomersProxyHandler;


public class SalesOrderHandler implements HandlerInterface {

//	private SuperMarketSalesDAO salesDAO = new SuperMarketSalesDAO();
	List salesList;
	private Invoice invoice = new Invoice();
	String status;
	List<SalesOrder> salesOrderList;

	SuperMarketSalesInterface salesDAO;


	public SalesOrderHandler() {
	    salesDAO = (SuperMarketSalesInterface) Proxy.newProxyInstance(
	            CustomersProxyHandler.class.getClassLoader(),
	            new Class[] {SuperMarketSalesInterface.class},
	            new CustomersProxyHandler(new SuperMarketSalesDAO())
	    );
	}
	@Override
	public String excute(HttpServletRequest request, HttpServletResponse response) {
		String httpMethod = request.getMethod();

		System.out.println("Request url Http Method :" + httpMethod);
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
		salesList = salesDAO.getAllSalesDeatils_v1();
		System.out.println("Super Market Billing Application salesRecordsList for api call " + salesList);
		 request.setAttribute("salesList", salesList);
         Gson gson = new Gson();
//          System.out.println("Product map :"+groupProducts(productsList));
         String json = gson.toJson(salesList);
         try {
         response.setContentType("application/json");
         response.setCharacterEncoding("UTF-8");

			response.getWriter().write(json);

         response.getWriter().close();
         }catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 			return "error";
 		}
		return "success";
	}

	@Override
	public String handlePost(HttpServletRequest request, HttpServletResponse response) {

		return printRequest(request);
	}

	public String printRequest(HttpServletRequest request) {
		String result = null;
		try {
			BufferedReader reader = request.getReader();
			StringBuilder requestBody = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				requestBody.append(line);
			}
			String requestBodyString = requestBody.toString();
			String totalAmountPart = requestBodyString.split("\"totalAmount\":")[1].split("}")[0];
			String value = totalAmountPart.trim().replaceAll("\"", "");
			System.out.println("total amount :"+value);
			double totalAmount = Double.parseDouble(value);
			convert(requestBodyString);
			result = saveInvoiceDetails(convert(requestBodyString), totalAmount);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return result;
	}

	public static List<SalesOrder> convert(String json) {
		List<SalesOrder> salesOrders = new ArrayList<>();

		Pattern itemsPattern = Pattern.compile("\"items\":\\[(.*?)\\]");
		Matcher itemsMatcher = itemsPattern.matcher(json);
		if (itemsMatcher.find()) {
			String itemsString = itemsMatcher.group(1);

			System.out.println("itemsString " + itemsString);
			String[] itemsArray = itemsString.split("\\},\\{");
			System.out.println("itemsArray " + Arrays.toString(itemsArray));
			for (String item : itemsArray) {

				item = item.replaceAll("\\{", "").replaceAll("\\}", "");
				System.out.println("item " + item);

				String[] keyValuePairs = item.split(",");
				Map<String, String> itemMap = new HashMap<>();
				for (String pair : keyValuePairs) {
					String[] parts = pair.split(":");
					if (parts.length == 2) {
						String key = parts[0].trim().replaceAll("\"", "");
						String value = parts[1].trim().replaceAll("\"", "");
						itemMap.put(key, value);
					}
				}

				SalesOrder salesOrder = new SalesOrder();
				salesOrder.setCustomerId(Integer.parseInt(itemMap.get("customerId")));
				salesOrder.setCustomerName(itemMap.get("customerName"));
				salesOrder.setCustomerPhone(itemMap.get("customerPhone"));
				salesOrder.setProduct(itemMap.get("product"));
				salesOrder.setQuantity(Integer.parseInt(itemMap.get("quantity")));
				salesOrder.setAmount(Double.parseDouble(itemMap.get("amount")));

				salesOrders.add(salesOrder);
			}
		}
		System.out.println(salesOrders);

		return salesOrders;
	}

	public String saveInvoiceDetails(List<SalesOrder> items, Double totalAmount) {
		System.out
				.println("Super Market Billing Application SMInvoiceAction saveInvoiceDetails for api call " + invoice);
//	    	List<SalesOrder> items = invoice.getItems();
//	    	Double totalAmount=invoice.getTotalAmount();
		int cId = 0;
		boolean allSaved = true;

		for (SalesOrder item : items) {
			boolean isSaved = salesDAO.saveSalesOrder_v1(item);
			if (!isSaved) {
				System.out.println("Enter into saveInvoiceDetails " + item);
				allSaved = false;
				break;
			}
			cId = item.getCustomerId();
		}
		status = salesDAO.updateCreditPoints_v1(cId, 500) ? "success" : "error";

		if (allSaved) {
			status = "success";
			System.out.println(
					"Enter into saveInvoiceDetails if  before insert invoide details status" + status + "Cid :" + cId);
			status = salesDAO.saveInvoiceDetails_v1(cId, totalAmount) ? "success" : "error";
			System.out.println(
					"Enter into saveInvoiceDetails if After insert invoide details status" + status + " Cid :" + cId);
			status = salesDAO.updateCreditPoints_v1(cId, totalAmount) ? "success" : "error";
			System.out.println("Enter into saveInvoiceDetails if After updateCreditPoints details status " + status
					+ " Cid :" + cId);
		} else {
			status = "error";
			System.out.println("Enter into saveInvoiceDetails else status" + status);
		}
		System.out.println("Enter into saveInvoiceDetails outer status" + status);
		return "success";
	}

	public String getAllSalesRecords() {
		salesList = salesDAO.getAllSalesDeatils_v1();
		System.out.println("Super Market Billing Application salesRecordsList for api call " + salesList);

		return "success";
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
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
