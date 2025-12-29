package com.rt.engine.common.util;

import java.util.Map;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSONObject;
import com.rt.engine.common.component.ApplicationContextUtil;
import com.rt.engine.common.constants.EngineConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtil {

    public static HttpMethod getHttpMethod(String method) {
        HttpMethod httpMethod = null;
        if (HttpMethod.GET.name().equalsIgnoreCase(method)) {
            httpMethod = HttpMethod.GET;
        } else if (HttpMethod.POST.name().equalsIgnoreCase(method)) {
            httpMethod = HttpMethod.POST;
        } else if (HttpMethod.DELETE.name().equalsIgnoreCase(method)) {
            httpMethod = HttpMethod.DELETE;
        } else if (HttpMethod.PUT.name().equalsIgnoreCase(method)) {
            httpMethod = HttpMethod.PUT;
        }
        return httpMethod;
    }

    public static ResponseEntity<JSONObject> send(String url, HttpMethod httpMethod, String token,
        Map<String, String> requestParams, Map<String, Object> uriVariables, JSONObject requestBody) {
        log.info("requestBody is " + requestBody);
        RestTemplate restTemplate = ApplicationContextUtil.getBean(RestTemplate.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add(EngineConstants.INTERFACE_AUTHORIZATION, token);
        MediaType type = MediaType.parseMediaType("application/json; charset=utf-8");
        headers.setContentType(type);
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, headers);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
        if (requestParams != null) {
            MultiValueMap<String, String> urlParamsMap = new LinkedMultiValueMap<>();
            urlParamsMap.setAll(requestParams);
            uriComponentsBuilder.queryParams(urlParamsMap);
        }
        if (uriVariables != null) {
            uriComponentsBuilder.uriVariables(uriVariables);
        }
        url = uriComponentsBuilder.build().toUri().toString();
        log.info("url is " + url);

        ResponseEntity<JSONObject> responseEntity =
            restTemplate.exchange(url, httpMethod, requestEntity, JSONObject.class);
        return responseEntity;
    }
}
