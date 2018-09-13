package com.chinadovey.parking.core.supports.spring;

import java.lang.annotation.Annotation;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 暂不启用
 * @author Bean
 *
 */
@Component
@Aspect
public class AopSecurityURLAccessCheck {
	
	private static Logger logger = Logger.getLogger(AopSecurityURLAccessCheck.class);
	
	@Around(value = "execution(* com.chinadovey..*.controller..*.*(..))")
	public Object around(ProceedingJoinPoint point) throws Throwable {

		MethodSignature signature = (MethodSignature)point.getSignature();
		
		if(logger.isDebugEnabled()){
			logger.debug("========begin AOP:URL Access Check ... ========");
			logger.debug("Aop-Target:"+point.getTarget());
			logger.debug("Aop-Method:"+signature.getName());
		}
		
		for(Annotation anno : signature.getMethod().getAnnotations()){
			if(logger.isDebugEnabled())
				logger.debug("Aop-Method-Annotation:"+anno);
		}
		
//		SecurityAccessCheckable checkable = signature.getMethod().getAnnotation(SecurityAccessCheckable.class);
		
//		if(checkable!=null){
//			SessionOpt.getSecuObject(session);
//		}
		
		if(logger.isDebugEnabled())
			logger.debug("========end AOP:URL Access Check ... ========");
		
		return point.proceed();
	}
}
