package com.chinadovey.parking.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识Controller的方法由ACL控制
 * @author Bean
 *
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityAccessCheckable {
	/**
	 * 资源
	 * @return
	 */
	Class<?> resource();
	/**
	 * 操作（这个参数使用方法待定，目前默认为View）
	 * @return
	 */
	Operation operation() default Operation.VIEW;
	
	enum Operation{
		VIEW,
		EDIT,
		DELE,
		CRET
	}
}