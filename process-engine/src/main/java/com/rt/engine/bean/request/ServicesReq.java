package com.rt.engine.bean.request;

import lombok.Data;

@Data
public class ServicesReq {

    /**
     * 服务id
     */
    private String serviceId;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 服务描述
     */
    private String serviceDescribe;

    /**
     * 资源来源(0 自行新增 1 同步新增)
     */
    private Integer sourceIn;

    /**
     * 认证方式
     */
    private String authenticationMethod;

    /**
     * 接口描述
     */
    private String interfaceDescribe;

    /**
     * 接口地址
     */
    private String interfaceAddr;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求体
     */
    private String requestBody;


    /**
     * 返回体
     */
    private String responseBody;

    /**
     * 请求示例
     */
    private String requestExample;

    /**
     * 返回示例
     */
    private String responseExample;

    /**
     * 删除状态(0 未删除， 1删除)
     */
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
}
