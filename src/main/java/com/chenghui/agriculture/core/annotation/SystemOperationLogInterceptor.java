package com.chenghui.agriculture.core.annotation;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.service.appLogin.Memory;
import com.chenghui.agriculture.service.appLogin.ThreadTokenHolder;
import com.chenghui.agriculture.service.system.SystemOperationLogService;

/**
 * @author yudq
 * @date 2015年6月3日
 *
 */
@Aspect
@Component
public class SystemOperationLogInterceptor {

	@Autowired
	private SystemOperationLogService systemOperationLogService;
	
	@Autowired
	private Memory memory;

	@Pointcut("execution(public * com.chenghui.agriculture.service.*.impl.*.*(..))")
	public void systemOperationLog() {
	}

	@Around(value = "systemOperationLog() && @annotation(annotation) &&args(object,..) ", argNames = "annotation,object")
	public Object interceptorAppLogic(ProceedingJoinPoint joinPoint, SystemOperationLogAnnotation annotation,
			Object object) throws Throwable {

		String temp = joinPoint.getStaticPart().toShortString();
		String longTemp = joinPoint.getStaticPart().toLongString();
		String classType = joinPoint.getTarget().getClass().getName();
		String methodName = temp.substring(10, temp.length() - 1);
		String methodName_only = joinPoint.getSignature().getName();
		Class<?> className = Class.forName(classType);
		// 日志动作
//		String extra = null;
		@SuppressWarnings("rawtypes")
		Class[] args = new Class[joinPoint.getArgs().length];
		String[] sArgs = (longTemp.substring(longTemp.lastIndexOf("(") + 1, longTemp.length() - 2)).split(",");
		for (int i = 0; i < args.length; i++) {
			if (sArgs[i].endsWith("[]")) {
				args[i] = Array.newInstance(Class.forName("java.lang.String"), 1).getClass();
			} else {
				args[i] = Class.forName(sArgs[i]);
			}
		}
		Method method = className.getMethod(methodName.substring(methodName.indexOf(".") + 1, methodName.indexOf("(")),
				args);

		if (method.isAnnotationPresent(SystemOperationLogAnnotation.class)) {
			SystemOperationLogAnnotation logAnnotation = method.getAnnotation(SystemOperationLogAnnotation.class);
			String operateDescribe = logAnnotation.opText();
			// 获取被注解方法的参数，实现动态注解
			String logArg = "";
			for (int j = 0; j < args.length; j++) {
				if (logArg.length() > 0)
					logArg += ";";
				if (args[j].getName() == "java.lang.Long") {
					logArg += String.valueOf((joinPoint.getArgs())[0]);
				} else if (args[j] == String[].class) {
					String[] arrayArg = (String[]) (joinPoint.getArgs())[0];
					logArg += Arrays.toString(arrayArg);
				} else if (args[j].getName().startsWith("com.chenghui.agriculture.model")) {

					logArg += joinPoint.getArgs()[j].toString();
				}
			}
			// 将注解中%转换为被拦截方法参数中的id
			operateDescribe = operateDescribe.indexOf("%") != -1 ? operateDescribe.replace("%", logArg)
					: operateDescribe;
			// 以下是数据库操作
			ShiroUser shiroUser = null;
			try{
				if (SecurityUtils.getSubject().getPrincipals() != null) {
					shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
				}else if (StringUtils.isNotEmpty(ThreadTokenHolder.getToken())) {
					shiroUser = (ShiroUser)(memory.getObjectValue(ThreadTokenHolder.getToken()));
				}
			}catch(Exception ex){
				ex.printStackTrace();
				shiroUser = null;
				
			}
			
			String opType = annotation.opType();
			String id = "";
			if (args.length == 1) {
				if (args[0].getName() == "java.lang.Long") {
					id = logArg;
					// 先特殊处理一下
					logArg = getlogArgs(classType, methodName_only, logArg);
				}
			}
			systemOperationLogService.saveSystemOperationLog(shiroUser, annotation.appModule(), opType, operateDescribe,
					logArg, id);
		}
		return joinPoint.proceed();
	}

	private String getlogArgs(String classType, String methodName_only, String logArg) {

		return "";
	}

}
