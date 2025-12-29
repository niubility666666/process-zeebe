package com.rt.engine.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApaasServiceInfo {
    /** 服务ID **/
    private String serviceId;
    /** 服务名称 **/
    private String serviceName;
    /** 服务描述 **/
    private String serviceDesc;
    /** 服务地址 **/
    private String serviceUrl;
    /** 请求方式 **/
    private String httpMethod;
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
    /** 是否删除 **/
    private String isDeleted;
    /** 创建时间 **/
    private String createdTime;
    /** 创建人 **/
    private String createdBy;
}
