package handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.crm.dao.SuperMarketSalesDAO;
import com.crm.model.Product;
import com.google.gson.Gson;

public class ProductHandler implements HandlerInterface{

	private SuperMarketSalesDAO productsDAO = new SuperMarketSalesDAO();
	List<Product> productsList;

	public ProductHandler() {
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
	             productsList = productsDAO.getAllAvailableProducts();

	            System.out.println("Product list for API call: " + productsList);
	            request.setAttribute("productsList", productsList);
	            Gson gson = new Gson();
//	             System.out.println("Product map :"+groupProducts(productsList));
	            String json = gson.toJson(productsList);

	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(json);
	            response.getWriter().close();
	            return "success";
	        } catch (Exception e) {
	            System.out.println("Failed to fetch products: " + e.getMessage());
	            return "error";
	        }
	}

	@Override
	public String handlePost(HttpServletRequest request, HttpServletResponse response) {
		try {
            productsList = addProduct(request);
           System.out.println("Product list for API call addProduct : " + productsList);
           productsDAO.addProducts(getProductFromRequest(request));
//           getProductFromRequest(request);
           RequestDispatcher dispatcher = request.getRequestDispatcher("/WebAppSuperMarket/pages/products.jsp");
           dispatcher.forward(request, response);
           return "success";
       } catch (Exception e) {
           System.out.println("Failed to fetch products: " + e.getMessage());
           return "error";
       }
	}

	@Override
	public String handlePut(HttpServletRequest request, HttpServletResponse response) {
		try {
           System.out.println("Product list for API call updateProduct : " + productsList);
           productsDAO.UpdateProductDetails(getProductFromRequest(request));
           return "success";
       } catch (Exception e) {
           System.out.println("Failed to fetch products: " + e.getMessage());
           return "error";
       }
	}

	@Override
	public String handleDelete(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Product> addProduct(HttpServletRequest request) {
        try {
            Product product = new Product();
            product.setProductId(Integer.parseInt(request.getParameter("product.productId")));
            product.setProductName(request.getParameter("product.productName"));
            product.setCategory(request.getParameter("product.category"));
            product.setPrice(Double.parseDouble(request.getParameter("product.price")));
            product.setStock(Integer.parseInt(request.getParameter("product.stock")));
            product.setVendorId(Integer.parseInt(request.getParameter("product.vendorId")));

            System.out.println("Add products :"+product);
            productsDAO.addProducts(product);
            return productsDAO.getAllAvailableProducts(); // refresh list
        } catch (Exception e) {
            System.out.println("Failed to add product: " + e.getMessage());
            return productsList;
        }
    }

    public List<Product> updateProduct(HttpServletRequest request) {
        try {
            Product product = new Product();
            product.setProductId(Integer.parseInt(request.getParameter("product.productId")));
            product.setProductName(request.getParameter("product.productName"));
            product.setCategory(request.getParameter("product.category"));
            product.setPrice(Double.parseDouble(request.getParameter("product.price")));
            product.setStock(Integer.parseInt(request.getParameter("product.stock")));
            product.setVendorId(Integer.parseInt(request.getParameter("product.vendorId")));

            System.out.println("update products :"+product);
            productsDAO.UpdateProductDetails(product);
            return productsDAO.getAllAvailableProducts(); // refresh list
        } catch (Exception e) {
            System.out.println("Failed to update product: " + e.getMessage());
            return productsList;
        }
    }

    public static Map<String, List<Product>> groupProducts(List<Product> productList) {
        return productList.stream()
                .collect(Collectors.groupingBy(Product::getCategory));
    }

    public Product getProductFromRequest(HttpServletRequest request) {
    	BufferedReader reader;
    	Product product = new Product();
		try {
			reader = request.getReader();
			System.out.println("Request body "+request.getReader());
		String line;
        String item="";
		while ((line = reader.readLine()) != null) {
			item+=line;
		}

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
	        System.out.println("update itemMap products :"+itemMap);
	        product = new Product();
//            product.setProductId(Integer.parseInt(itemMap.get("productId")));
            product.setProductName(itemMap.get("productName"));
            product.setCategory(itemMap.get("category"));
            product.setPrice(Double.parseDouble(itemMap.get("price")));
            product.setStock(Integer.parseInt(itemMap.get("stock")));
            product.setVendorId(Integer.parseInt(itemMap.get("vendorId")));

            System.out.println("getProductFromRequest products :"+product);


		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
		return product;
    }
    
    
    public List getAction() {
    	List productsList= new ArrayList();
    	productsList = productsDAO.getAllAvailableProducts();
        System.out.println("Product list for API call: " + productsList);
        return productsList;
    }
    
    public String putAction(Map<String, String> itemMap) {
    	 System.out.println("update itemMap products :"+itemMap);
    	 if(!itemMap.isEmpty()) {
	     Product  product = new Product();
	     product.setProductId(Integer.parseInt(itemMap.get("productId")));
         product.setProductName(itemMap.get("productName"));
         product.setCategory(itemMap.get("category"));
         product.setPrice(Double.parseDouble(itemMap.get("price")));
         product.setStock(Integer.parseInt(itemMap.get("stock")));
         product.setVendorId(Integer.parseInt(itemMap.get("vendorId")));
         System.out.println("getProductFromRequest products put:"+product);
         productsDAO.UpdateProductDetails(product);
         return "success";
    	 }
    return "error";
    }
    
    public String postAction(Map<String, String> itemMap) {
   	 System.out.println("Create itemMap products :"+itemMap);
   	 if(!itemMap.isEmpty()) {
	     Product  product = new Product();
        product.setProductName(itemMap.get("productName"));
        product.setCategory(itemMap.get("category"));
        product.setPrice(Double.parseDouble(itemMap.get("price")));
        product.setStock(Integer.parseInt(itemMap.get("stock")));
        product.setVendorId(Integer.parseInt(itemMap.get("vendorId")));
        System.out.println("getProductFromRequest products post:"+product);
        productsDAO.addProducts(product);
        return "success";
   	 }
   return "error";
   }
    
    public String deleteAction() {
    	
    	return "error";
    }
}
