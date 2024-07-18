package com.crm.action;

import java.util.Map;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.dispatcher.mapper.DefaultActionMapper;

import com.opensymphony.xwork2.config.ConfigurationManager;

public class CustomActionMapper extends DefaultActionMapper{

	public ActionMapping getMapping(HttpServletRequest request, ConfigurationManager configManager) {
		
		String uri= request.getRequestURI();
		String httpMethod= request.getMethod();
		Map<String, ? extends Object> map = request.getParameterMap();
		 String[] parts = uri.split("/");
	     String resource = parts[parts.length - 1];
	     
	     System.out.println("Inside CustomActionMapper getMapping method");
	     
		if(resource.equals("Products")) {
//			mapping.setMethod("productMethod");
			request.setAttribute("handlerName", "ProductHandler");
			request.setAttribute("method", httpMethod);
			System.out.println("inside method if getMapping Products");
			return new ActionMapping(resource, "/api/*", "productMethod", (Map<String, Object>) map);
			
		}
		else if(resource.equals("Customers")) {
			request.setAttribute("handlerName", "customerHandler");
			System.out.println("inside method if Customers ");
			
		}
	     
	     else if(uri.contains("Zoho")) {
	    	 System.out.println("inside method if Zoho ");
	     }
		else {
			 System.out.println("inside method pages "+resource);
			return super.getMapping(request, configManager);
		}
		
		return new ActionMapping("Zoho", "/demo", "execute", (Map<String, Object>) map);
	}

}
