package com.rt.engine.common.aspect;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.rt.engine.common.annotation.OperationLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.rt.engine.bean.response.BaseResponse;
import com.rt.engine.common.constants.CodeEnum;
import com.rt.engine.common.constants.EngineConstants;
import com.rt.engine.mapper.OperationLogMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(2)
@Aspect
@Component
public class OperationLogAspect {

    @Resource
    private OperationLogMapper operationLogMapper;

    @AfterReturning(pointcut = "execution(* com.beagle.engine.controller..*.*(..))", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        try {
            // 接口调用成功才会记录操作日志
            if (result instanceof BaseResponse && CodeEnum.SUCCESS.getCode().equals(((BaseResponse)result).getCode())) {
                // 认证成功的才会记录操作日志

                MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
                OperationLog operationLog = methodSignature.getMethod()
                    .getDeclaredAnnotation(OperationLog.class);
                // 判断是否有注解并需要入库
                if (operationLog != null && operationLog.record()) {
                    ServletRequestAttributes servletRequestAttributes =
                        (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

                    HttpServletRequest request = servletRequestAttributes.getRequest();
                    String userId = (String)request.getAttribute(EngineConstants.USER_ID);

                    com.rt.engine.bean.entity.OperationLog operationLogEntity = com.rt.engine.bean.entity.OperationLog.builder().operator(userId)
                        .recordDesc(operationLog.desc()).recordType(operationLog.type().name()).build();
                    log.info(JSONObject.toJSONString(operationLogEntity));
                    operationLogMapper.insertOperationLogEntity(operationLogEntity);
                }
            }
        } catch (Exception ex) {
            log.error("OperationLogAspect Exception:", ex);
        }
    }
}
