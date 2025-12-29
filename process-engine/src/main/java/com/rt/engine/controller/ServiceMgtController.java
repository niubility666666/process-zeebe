package com.rt.engine.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.rt.engine.bean.request.ServicesReq;
import com.rt.engine.bean.request.TestServiceReq;
import com.rt.engine.bean.response.BaseResponse;
import com.rt.engine.bean.response.PageResponseVO;
import com.rt.engine.bean.response.ResponseVO;
import com.rt.engine.service.ServiceMgtService;
import com.github.pagehelper.PageHelper;

@RestController
@RequestMapping("service")
public class ServiceMgtController {

    @Resource
    private ServiceMgtService serviceMgtService;

    @PostMapping("test")
    public ResponseVO testService(@RequestBody TestServiceReq testServiceReq) {
        HttpMethod httpMethod = null;
        if (HttpMethod.GET.name().equalsIgnoreCase(testServiceReq.getHttpMethod())) {
            httpMethod = HttpMethod.GET;
        } else if (HttpMethod.POST.name().equalsIgnoreCase(testServiceReq.getHttpMethod())) {
            httpMethod = HttpMethod.POST;
        } else if (HttpMethod.DELETE.name().equalsIgnoreCase(testServiceReq.getHttpMethod())) {
            httpMethod = HttpMethod.DELETE;
        } else if (HttpMethod.PUT.name().equalsIgnoreCase(testServiceReq.getHttpMethod())) {
            httpMethod = HttpMethod.PUT;
        }
        JSONObject jsonObject = new JSONObject();
        Map<String, String> requestParams = testServiceReq.getRequestParams();
        // 返回requestParams请求参数格式
        // if (requestParams != null && requestParams.size() > 0) {
        // ServiceField requestField = ServiceField.builder().type(ServiceUtil.OBJECT)
        // .children(ServiceUtil.convertServiceField(JSON.parseObject(JSON.toJSONString(requestParams)))).build();
        // jsonObject.put("requestField", requestField);
        // }
        // JSONObject requestBody = testServiceReq.getRequestBody();
        // // 返回requestBody请求参数格式
        // if (requestBody != null && requestBody.keySet().size() > 0) {
        // ServiceField requestField = ServiceField.builder().type(ServiceUtil.OBJECT)
        // .children(ServiceUtil.convertServiceField(requestBody)).build();
        // jsonObject.put("requestField", requestField);
        // }
        // try {
        // ResponseEntity<JSONObject> responseEntity = HttpUtil.send(testServiceReq.getUrl(), httpMethod,
        // testServiceReq.getToken(), testServiceReq.getRequestParams(), testServiceReq.getUriVariables(),
        // testServiceReq.getRequestBody());
        // if (responseEntity.getStatusCode() == HttpStatus.OK) {
        // jsonObject.put("message", HttpStatus.OK.getReasonPhrase());
        // ServiceField responseField = ServiceField.builder().type("object")
        // .children(ServiceUtil.convertServiceField(responseEntity.getBody())).build();
        // jsonObject.put("responseField", responseField);
        // }
        // } catch (Exception exception) {
        // jsonObject.put("message", exception.getMessage());
        // }
        return ResponseVO.success(jsonObject);
    }

    @GetMapping("/{id}")
    public ResponseVO queryServiceById(@PathVariable String id) {
        return ResponseVO.success(null);
    }

    @PostMapping("/insertService")
    public BaseResponse insertService(@RequestBody ServicesReq servicesReq) {
        return BaseResponse.success();
    }

    @PutMapping("/updateService")
    public BaseResponse updateService(@RequestBody ServicesReq servicesReq) {
        return BaseResponse.success();
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteServiceById(@PathVariable String id) {

        return BaseResponse.success();
    }

    @GetMapping("/list/{limit}/{page}")
    public PageResponseVO queryServiceList(@PathVariable int limit, @PathVariable int page,
        @RequestParam(required = false) String serviceName) {
        PageHelper.startPage(page, limit);
        return PageResponseVO.success(serviceMgtService.queryServiceList(serviceName));
    }

    @GetMapping("/name")
    public ResponseVO queryServiceNameList(@RequestParam(required = false) String serviceName) {
        return ResponseVO.success(serviceMgtService.queryServiceNameList(serviceName));
    }

    @GetMapping("/detail")
    public ResponseVO queryServiceInfoById(@RequestParam String serviceId) {
        return ResponseVO.success(serviceMgtService.queryServiceInfoById(serviceId));
    }
}
