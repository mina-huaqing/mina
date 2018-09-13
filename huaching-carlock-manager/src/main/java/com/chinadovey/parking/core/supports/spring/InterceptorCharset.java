package com.chinadovey.parking.core.supports.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.chinadovey.parking.Constants;

/**
 * 编码拦截器
 * @author Bean
 *
 */
public class InterceptorCharset extends HandlerInterceptorAdapter {

	private static Logger logger = Logger.getLogger(InterceptorCharset.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("========Interceptor-preHandle:Do Something here...");
			logger.debug("Interceptor-Handler:" + handler);
		}
		
		request.setCharacterEncoding(Constants.CHARSET);
		response.setCharacterEncoding(Constants.CHARSET);
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {}
}
