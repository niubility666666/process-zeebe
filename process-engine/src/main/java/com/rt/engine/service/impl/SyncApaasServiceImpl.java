package com.rt.engine.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rt.engine.bean.dto.ServiceField;
import com.rt.engine.bean.entity.ServiceInfo;
import com.rt.engine.common.config.BeagleConfig;
import com.rt.engine.mapper.ApaasServiceMapper;
import com.rt.engine.mapper.ServiceMgtMapper;
import com.rt.engine.service.SyncApaasService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SyncApaasServiceImpl implements SyncApaasService {

    @Resource
    private ApaasServiceMapper apaasServiceMapper;
    @Resource
    private ServiceMgtMapper serviceMgtMapper;
    @Resource
    private BeagleConfig beagleConfig;

    @Override
    public void syncApaasService() {
        serviceMgtMapper.deleteApaasService();
        int pageSize = 100;
        int nextPage;
        PageInfo<ServiceInfo> pageInfo = getSyncServiceInfo(1, pageSize);
        nextPage = pageInfo.getNextPage();
        convertApaasService(pageInfo.getList());
        while (nextPage > 0) {
            pageInfo = getSyncServiceInfo(pageInfo.getNextPage(), pageSize);
            nextPage = pageInfo.getNextPage();
            convertApaasService(pageInfo.getList());
        }
    }

    private PageInfo<ServiceInfo> getSyncServiceInfo(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ServiceInfo> serviceInfoList = apaasServiceMapper.queryApaasServiceList();
        return new PageInfo<>(serviceInfoList);
    }

    private void convertApaasService(List<ServiceInfo> apaasServiceInfoList) {
        if (apaasServiceInfoList != null && apaasServiceInfoList.size() > 0) {
            List<ServiceInfo> serviceInfoList = new ArrayList<>();
            for (ServiceInfo serviceInfo : apaasServiceInfoList) {
                serviceInfo.setServiceUrl(beagleConfig.apaasProxyUrl + "/" + serviceInfo.getServiceId() + "/service/"
                    + serviceInfo.getApaasApplyId() + "/0");
                serviceInfo.setRequestParam(
                    JSON.toJSONString(getServiceField(JSON.parseArray(serviceInfo.getRequestParam()))));
                serviceInfo
                    .setRequestBody(JSON.toJSONString(getServiceField(JSON.parseArray(serviceInfo.getRequestBody()))));
                serviceInfo.setResponse(JSON.toJSONString(getServiceField(JSON.parseArray(serviceInfo.getResponse()))));
                serviceInfo.setSource("apaas");
                serviceInfoList.add(serviceInfo);
            }
            // 批量入库
            serviceMgtMapper.batchInsertService(serviceInfoList);
        }
    }

    private List<ServiceField> getServiceField(JSONArray jsonArray) {
        List<ServiceField> serviceFieldList = new ArrayList<>();
        if (jsonArray != null && jsonArray.size() > 0) {
            for (Object object : jsonArray) {
                if (object instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject)object;
                    ServiceField serviceField = new ServiceField();
                    Object paramName = jsonObject.get("name");
                    if (paramName instanceof String) {
                        serviceField.setParamName(String.valueOf(paramName));
                    }
                    Object paramType = jsonObject.get("field_type");
                    if (paramType instanceof String) {
                        serviceField.setParamType(String.valueOf(paramType));
                    }
                    Object paramDesc = jsonObject.get("descript");
                    if (paramDesc instanceof String) {
                        serviceField.setParamDesc(String.valueOf(paramDesc));
                    }
                    Object paramValue = jsonObject.get("example");
                    if (paramValue instanceof String) {
                        serviceField.setParamValue(String.valueOf(paramValue));
                    }
                    if ("1".equals(jsonObject.get("is_must"))) {
                        serviceField.setRequired(true);
                    }
                    Object children = jsonObject.get("children");
                    if (children instanceof JSONArray) {
                        serviceField.setChildren(getServiceField((JSONArray)children));
                    }
                    serviceFieldList.add(serviceField);
                }
            }
        }
        return serviceFieldList;
    }
}
