package com.chinadovey.parking.core.supports.spring;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 当前线程数据传递工具类
 * @author Bean
 *
 */
public class LocalObjects {
	private static ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<HttpServletResponse> responses = new ThreadLocal<HttpServletResponse>();
	private static ThreadLocal<Map<String,Object>> localDatas = new ThreadLocal<Map<String,Object>>();
	
	public static void setRequest(HttpServletRequest request){
		requests.set(request);
	}
	public static HttpServletRequest getRequest(){
		return requests.get();
	}
	public static void setResponse(HttpServletResponse response){
		responses.set(response);
	}
	public static HttpServletResponse getResponse(){
		return responses.get();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T get(String key){
		Map<String,Object> m = localDatas.get();
		if(m!=null){
			return (T) m.get(key);
		}
		return null;
	}
	
	public static void set(String key,Object value){
		Map<String,Object> m = localDatas.get();
		if(m==null){
			m = new HashMap<String,Object>();
		}
		m.put(key, value);
		localDatas.set(m);
	}
}
