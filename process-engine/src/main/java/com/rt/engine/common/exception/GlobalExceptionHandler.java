package com.rt.engine.common.exception;

import com.rt.engine.common.constants.CodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rt.engine.bean.response.BaseResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ProcessEngineException.class)
    public BaseResponse beagleExceptionHandler(ProcessEngineException exception) {
        log.error("GlobalExceptionHandler.ProcessEngineException", exception);
        BaseResponse baseResponse = BaseResponse.fail();
        if (!StringUtils.isBlank(exception.getErrorCode())) {
            baseResponse.setCode(exception.getErrorCode());
            baseResponse.setDesc(exception.getErrorMsg());
        }
        return baseResponse;
    }

    @ExceptionHandler(value = Exception.class)
    public BaseResponse exceptionHandler(Exception exception) {
        log.error("GlobalExceptionHandler.exceptionHandler", exception);
        return BaseResponse.fail();
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public BaseResponse exceptionHandler(MethodArgumentTypeMismatchException exception) {
        log.error("GlobalExceptionHandler.MethodArgumentTypeMismatchException", exception);
        BaseResponse baseResponse = BaseResponse.fail();
        if (!StringUtils.isBlank(exception.getErrorCode())) {
            baseResponse.setCode(CodeEnum.PARAMETER_ERROR.getCode());
            baseResponse.setDesc("入参参数类型不对");
        }
        return baseResponse;
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public BaseResponse httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.error("GlobalExceptionHandler.MethodArgumentTypeMismatchException", exception);
        BaseResponse baseResponse = BaseResponse.fail();
        baseResponse.setCode(CodeEnum.PARAMETER_ERROR.getCode());
        baseResponse.setDesc("http请求类型不对");
        return baseResponse;
    }
}
