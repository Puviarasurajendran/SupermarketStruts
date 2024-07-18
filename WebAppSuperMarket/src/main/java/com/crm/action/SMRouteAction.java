package com.crm.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;


import com.opensymphony.xwork2.ActionSupport;

import handler.HandlerInterface;
import handler.ProductHandler;
import jsonbodyutils.JsonBodyUtils;

public class SMRouteAction extends ActionSupport implements ServletRequestAware , ServletResponseAware {



    HttpServletRequest request;
	HttpServletResponse response;
    JsonBodyUtils jsonUtil= new JsonBodyUtils();
	
//	Invoice invoice = new Invoice();
//	CustomerHandler customerHandler= new CustomerHandler();
//	ProductHandler  productHandler = new ProductHandler();
//	VendorHandler vendorHandler = new VendorHandler();
//	SalesOrderHandler salesOrderHandler= new SalesOrderHandler();
//	CustomersProxyHandler cusomersProxyHandler = new CustomersProxyHandler(salesOrderHandler);
	String result;

	@Override
	public void setServletRequest(HttpServletRequest req) {
		this.request=req;
	}

	@Override
	public void setServletResponse(HttpServletResponse res) {
		 this.response=res;
	}

    @Override
	public String execute() {
    	System.out.println("Super Market Billing Application inside execute method");
       return route();
    }

    public String route() {
    	System.out.println("Super Market Billing Application Route method");
        String uri = request.getRequestURI();
        String[] parts = uri.split("/");
        String resource = parts[parts.length - 1];
        System.out.println("Url :"+Arrays.toString(parts));
        
//       if(!parts[parts.length - 2].equals("pages")){
        resource=actionHandler(resource);
        try {
			Class<?> handlerClass= Class.forName("handler."+resource+"Handler");
			HandlerInterface actionObject = (HandlerInterface) handlerClass.getDeclaredConstructor().newInstance();
            result= actionObject.excute(request, response);
        	
        	System.out.println(request.getAttribute("handlerName"));

		} catch (Exception e) {

			e.printStackTrace();
		}
//       }

        return "success";
    }
    
    
    public String productMethod() {
    	
    	String httpMethod = request.getMethod();
		   System.out.println("Request url Http Method :"+httpMethod);
		   try {
			HandlerInterface handlerAction= (HandlerInterface) Class.forName("handler."+request.getAttribute("handlerName")).getDeclaredConstructor().newInstance();
		
	        switch (httpMethod) {
	            case "GET":	
	            	result=jsonUtil.responseWriter(response, handlerAction.getAction());
	                return result;
	            case "POST":
	            	result= handlerAction.postAction(jsonUtil.readResponse(request));
	            	return result;
	            case "PUT":
	                result=	handlerAction.putAction(jsonUtil.readResponse(request));
	                return result;
	            case "DELETE":
	            	result= handlerAction.deleteAction();
	            	return result;
	            default:
	                throw new UnsupportedOperationException("HTTP method not supported: " + httpMethod);
	        }
		   } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			   result="error";
				e.printStackTrace();
			}
		 return result;
}

	public String actionHandler(String originalString) {
        int dotIndex = originalString.indexOf(".");
        String result;

        if (dotIndex != -1) {
            result = originalString.substring(0, dotIndex-1);
        } else {
            result = originalString.substring(0, originalString.length()-1);
        }
        System.out.println("Modified string actionHandler : " + result);
        return result;
    }

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}


}
