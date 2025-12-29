package com.rt.engine.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.rt.engine.common.constants.OperationType;

@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {
    /**
     * 操作日志描述
     */
    String desc();

    /**
     * 操作日志类型
     */
    OperationType type();

    /**
     * 是否记录 设置false 则不做任何处理
     */
    boolean record() default true;
}
