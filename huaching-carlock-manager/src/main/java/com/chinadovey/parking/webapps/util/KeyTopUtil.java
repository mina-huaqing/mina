package com.chinadovey.parking.webapps.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KeyTopUtil {
	
	public static Map<String,String> getHeader(){
		Map<String , String> headMap = new HashMap<String , String>();
		headMap.put("Content-Type", "application/json"); 
		headMap.put("charset", "UTF-8"); 
		
		return headMap;
	}
 
}
