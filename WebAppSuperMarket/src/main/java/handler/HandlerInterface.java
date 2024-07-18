package handler;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterface {

	String handleGet(HttpServletRequest request, HttpServletResponse response);
	String handlePost(HttpServletRequest request, HttpServletResponse response);
	String handlePut(HttpServletRequest request, HttpServletResponse response);
	String handleDelete(HttpServletRequest request, HttpServletResponse response);
	String excute(HttpServletRequest request,HttpServletResponse response);
	List getAction();
	String putAction(Map<String, String> itemMap);
	String postAction(Map<String, String> itemMap);
	String deleteAction();
}