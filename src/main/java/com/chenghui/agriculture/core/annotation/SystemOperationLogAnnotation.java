package com.chenghui.agriculture.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yudq
 * @date 2015年6月3日
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SystemOperationLogAnnotation {

	//模块名称
	String appModule();
	
	//操作类型: Login, Add, Remove, Update
	String opType();
	
	//操作类容
	String opText();
}
