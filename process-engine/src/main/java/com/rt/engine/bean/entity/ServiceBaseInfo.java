package com.rt.engine.bean.entity;

import lombok.Data;

@Data
public class ServiceBaseInfo {
    /** 服务ID **/
    private String serviceId;
    /** 服务名称 **/
    private String serviceName;
    /** 请求方式 **/
    private String httpMethod;
    /** 服务描述 **/
    private String serviceDesc;
    /** 服务地址 **/
    private String serviceUrl;
    /** 授权类型 **/
    private String authType;
    /** 授权token **/
    private String authToken;
    /** 请求参数url **/
    private String queryParam;
    /** 请求参数body **/
    private String queryBody;
    /** 响应报文 **/
    private String response;
    /** 服务来源 **/
    private String source;
    /** 创建时间 **/
    private String createdTime;
    /** 创建人 **/
    private String createdBy;
    /** 更新时间 **/
    private String updatedTime;
    /** 更新人 **/
    private String updatedBy;
}
