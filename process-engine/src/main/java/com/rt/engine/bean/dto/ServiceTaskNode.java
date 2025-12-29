package com.rt.engine.bean.dto;

import java.util.List;

import com.rt.engine.bean.entity.ServiceTaskVariable;

import lombok.Data;

@Data
public class ServiceTaskNode {
    /**
     * 流程ID
     */
    private String processId;
    /**
     * 节点ID
     */
    private String elementId;
    /**
     * 节点名称
     */
    private String elementName;
    /**
     * 服务ID
     */
    private String serviceId;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 输入
     */
    private List<ServiceTaskVariable> inputVariableList;
    /**
     * 输出
     */
    private List<ServiceTaskVariable> outputVariableList;
    /**
     * 超时时长
     */
    private Integer timeout;
    /**
     * 超时时长单位：s:秒 m: 分钟
     */
    private String timeoutUnit;
    /**
     * 重试次数
     */
    private Integer retryNum;
}
