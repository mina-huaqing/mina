package com.chinadovey.parking.core.supports.spring;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

/**
 * 全局的异常处理对象<br>
 * 待完善
 * @author Bean
 *
 */
public class DoveyHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {

		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		if(handler instanceof HandlerMethod){
			if (request.getHeader("x-requested-with") != null
			        && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
				
//				AjaxResult ar = new AjaxResult(Result.FAIL, ex.getMessage());
//				MappingJacksonJsonView view = new MappingJacksonJsonView();
//				view.setAttributesMap(JSONObject.fromObject(ar));
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("exception", ex);
//				return new ModelAndView(view);
				return new ModelAndView("/error_ajax",map);
			}else{
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("exception", ex);
				return new ModelAndView("/error",map);
			}
		}else{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("exception", ex);
			return new ModelAndView("/error",map);
		}
	}
}