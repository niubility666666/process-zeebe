package com.rt.engine.bean.entity;

import java.util.List;

import lombok.Data;

@Data
public class ServiceTaskVariable {
    /**
     * 参数ID
     */
    private String id;
    /**
     * 子节点
     */
    private List<ServiceTaskVariable> children;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 参数类型
     */
    private String paramType;
    /**
     * 必填
     */
    private boolean required;
    /**
     * 值域
     */
    private String paramValueType;
    /**
     * 参数值（示例值）
     */
    private Object paramValue;
    /**
     * 参数描述（说明）
     */
    private String paramDesc;
    /**
     * 父节点ID
     */
    private String parentId;
    /**
     * 流程ID
     */
    private String processId;
    /**
     * 节点ID
     */
    private String elementId;
    /**
     * 数据类型：输入/输出
     */
    private String dataType;
}
