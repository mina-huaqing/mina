package com.chinadovey.parking.webapps.util;

import java.util.HashMap;
import java.util.Map;

public class HttpsHeaderUtil {
	
	public static Map<String,String> getHeader(){
		Map<String , String> headMap = new HashMap<String , String>();
		headMap.put("Content-Type", "application/json"); 
		headMap.put("charset", "UTF-8"); 
		headMap.put("api_key", "DB9C22A225134C26B6AF2205C6D90E8C");
		headMap.put("api_secret", "39707FC804114C42B632A87C48357AB3");
		return headMap;
	}

}
