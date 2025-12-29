package com.rt.engine.bean.request;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class TestServiceReq {
    /**
     * 测试地址
     */
    @NotBlank
    private String url;
    /**
     * 请求方法
     */
    @NotBlank
    private String httpMethod;
    /**
     * get请求参数
     */
    private Map<String, String> requestParams;
    /**
     * url path 参数
     */
    private Map<String, Object> uriVariables;
    /**
     * 授权信息
     */
    private String token;
    /**
     * 请求参数
     */
    private JSONObject requestBody;
}
