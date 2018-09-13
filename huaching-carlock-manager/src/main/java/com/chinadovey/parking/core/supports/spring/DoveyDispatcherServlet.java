package com.chinadovey.parking.core.supports.spring;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 自定义的Spring启动器
 * @author Bean
 *
 */
public class DoveyDispatcherServlet extends DispatcherServlet {
	private static final long serialVersionUID = 5505792577358188028L;

	protected WebApplicationContext initWebApplicationContext() {
		WebApplicationContext context = super.initWebApplicationContext();
		ApplicationContextUtils.publish(context);		
		return context;
	}
}
