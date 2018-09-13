package com.chinadovey.parking.webapps.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfUtils {
	public static final String PROPERTIES_URL = "/config/parking.properties";
	
	private static Properties prop = new Properties ();
	static {
		try {
			prop.load(new InputStreamReader(ConfUtils.class.getResourceAsStream(PROPERTIES_URL), "UTF-8"));
		} catch (IOException e) {
			//
		}
	}
	
	public static String getConfigValue(String key){
		return prop.getProperty (key,"formal");
	}
	public static String getConfigValue(String key,String defaultValue){
		if(prop == null){
			return defaultValue;
		}
		return prop.getProperty (key,defaultValue);
	}
	
	public static Properties loadConf() throws IOException {
		return prop;
	}
	public static Properties loadConf(String res) throws IOException {
        Properties prop = new Properties();
        prop.load(new InputStreamReader(ConfUtils.class.getResourceAsStream(res), "UTF-8"));
		return prop;
	}
	
	public static String getControlAddress() throws IOException{
		return  prop.getProperty("dovey.cloud.node.localaddress");
	}
	
	public static String getJdbcAddress() throws IOException{
		return  prop.getProperty("proxool.jdbc.url");
	}
	public static String getJdbcUsername() throws IOException{
		return  prop.getProperty("proxool.jdbc.username");
	}
	public static String getJdbcPwd() throws IOException{
		return  prop.getProperty("proxool.jdbc.password");
	}

	public static String getCloudUrlHead() throws IOException {
		return  prop.getProperty("cloud.carlock.server");
	}
	public static String getCarLockUrlHead() throws IOException {
		return  prop.getProperty("carlock.url");
	}
	
	public static String getUrlByName(String paraName) throws IOException {
		return  prop.getProperty(paraName);
	}
	
	
	
	
}
