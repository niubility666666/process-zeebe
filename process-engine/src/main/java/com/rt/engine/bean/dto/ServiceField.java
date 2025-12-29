package com.rt.engine.bean.dto;

import java.util.List;

import lombok.Data;

@Data
public class ServiceField {
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 参数类型
     */
    private String paramType;
    /**
     * 参数值（示例值）
     */
    private Object paramValue;
    /**
     * 必填
     */
    private boolean required;
    /**
     * 参数描述（说明）
     */
    private String paramDesc;
    /**
     * 子节点
     */
    private List<ServiceField> children;
}
