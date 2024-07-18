package jsonbodyutils;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class JsonBodyUtils {
	
	public String responseWriter(HttpServletResponse response, List list) {
		  Gson gson = new Gson();
         String json = gson.toJson(list);
         try {
         response.setContentType("application/json");
         response.setCharacterEncoding("UTF-8");
         response.getWriter().write(json);
         response.getWriter().close();
         }
         catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
         return "success";
	}
	
	public Map<String, String>  readResponse(HttpServletRequest request) {
		Map<String, String> itemMap = new HashMap<>();
	 	BufferedReader reader;
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
		itemMap = new HashMap<>();
		for (String pair : keyValuePairs) {
			String[] parts = pair.split(":");
			if (parts.length == 2) {
				String key = parts[0].trim().replaceAll("\"", "");
				String value = parts[1].trim().replaceAll("\"", "");
				itemMap.put(key, value);
			}
		}
	}
		catch (Exception e) {
			e.printStackTrace();
		}
		return itemMap;
}
}
