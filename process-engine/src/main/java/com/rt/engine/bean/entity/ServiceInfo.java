package com.rt.engine.bean.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ServiceInfo {

    /**
     * 服务id
     */
    private String serviceId;
    /**
     * 服务申请的uuid
     */
    @JsonIgnore
    private String apaasApplyId;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 请求类型
     */
    private String httpMethod;

    /**
     * 服务描述
     */
    private String serviceDesc;

    /**
     * 接口地址
     */
    private String serviceUrl;
    /**
     * 认证方式
     */
    private String authType;
    /**
     * 认证参数
     */
    private String authValue;
    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 返回报文
     */
    private String response;

    /**
     * 是否删除
     */
    @JsonIgnore
    private Integer deleteFlag;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private String createdTime;

    /**
     * 修改人
     */
    private String updatedBy;

    /**
     * 修改时间
     */
    private String updatedTime;
    /**
     * 服务来源
     */
    private String source;
}