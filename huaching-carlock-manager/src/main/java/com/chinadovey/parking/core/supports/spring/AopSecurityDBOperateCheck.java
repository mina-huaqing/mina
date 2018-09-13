package com.chinadovey.parking.core.supports.spring;

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
public class AopSecurityDBOperateCheck {
	
	private static Logger logger = Logger.getLogger(AopSecurityDBOperateCheck.class);
	
	@Around(value = "execution(* com.chinadovey.apps..*.mappers..*.*(..))")
	public Object around(ProceedingJoinPoint point) throws Throwable {

		MethodSignature signature = (MethodSignature)point.getSignature();
		
		if(logger.isDebugEnabled()){
			logger.debug("========begin AOP:DB Operate Check ... ========");
			logger.debug("Target:"+point.getTarget().getClass().getName()+"!"+signature.getName()+"(");
			for(Class<?> type : signature.getParameterTypes()){
				logger.debug("\t"+type.getName());
			}
			logger.debug(")");
			logger.debug("========begin AOP:DB Operate Check ... ========");
		}
		Object result = point.proceed();
		return result;
	}
}
