package com.chinadovey.parking.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识Controller的方法不做任何安全检查，包括未登录的用户也能访问<br />
 * 如用于用户登录
 * @author Bean
 *
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityIgnoreHandler {
}


