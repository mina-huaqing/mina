package com.chinadovey.parking.core.supports.spring;

import org.springframework.context.ApplicationContext;

/**
 * Spring上下文工具类
 * @author Bean
 *
 */
public class ApplicationContextUtils {
	private static ApplicationContext applicationContext;
	
	static void publish(ApplicationContext applicationContext){
		ApplicationContextUtils.applicationContext = applicationContext;
	}
	
	/**
	 * 返回系统中的Spring上下文对象
	 * @return
	 */
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	/**
	 * 返回Spring管理的Bean
	 * @param type
	 * @return
	 */
	public static <T> T getBean(Class<T> type){
		return applicationContext.getBean(type);
	}
	
	/**
	 * 返回Spring管理的Bean
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name){
		return (T) applicationContext.getBean(name);
	}
}
