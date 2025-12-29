package com.rt.engine.common.aspect;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Description;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Order(1)
@Slf4j
@Aspect
@Component
public class InterfaceLogAspect {

    @Description("打印接口请求报文")
    @Before("execution(* com.beagle.engine.controller..*.*(..))")
    public void before(JoinPoint joinPoint) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
            String[] parameterNames = methodSignature.getParameterNames();
            Map<String, Object> paramMap = new HashMap<>(10);
            Object[] args = joinPoint.getArgs();
            if (parameterNames.length <= args.length) {
                for (int i = 0; i < parameterNames.length; i++) {
                    paramMap.put(parameterNames[i], args[i]);
                }
            }
            log.info(
                "interface          url : " + (attributes != null ? attributes.getRequest().getRequestURL() : null));
            log.info("interface    className : " + joinPoint.getTarget().getClass().getName());
            log.info("interface   methodName : " + joinPoint.getSignature().getName());
            log.info("interface  requestBody : " + JSONObject.toJSONString(paramMap));
        } catch (Exception exception) {
            log.error("InterfaceLogAspect.logInterfaceRequest Exception", exception);
        }
    }

    @Description("打印接口响应报文")
    @Around("execution(* com.beagle.engine.controller..*.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        log.info("interface responseBody : " + (result != null ? JSONObject.toJSONString(result) : null));
        log.info("interface time-consuming :{}", System.currentTimeMillis() - startTime);
        return result;
    }
}
