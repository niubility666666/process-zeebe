package com.rt.engine.service.impl;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.rt.engine.bean.dto.ServiceNameDTO;
import com.rt.engine.bean.entity.ServiceInfo;
import com.rt.engine.bean.request.ServicesReq;
import com.rt.engine.common.constants.EngineConstants;
import com.rt.engine.mapper.ServiceMgtMapper;
import com.rt.engine.service.ServiceMgtService;

@Service
public class ServiceMgtServiceImpl implements ServiceMgtService {

    @Resource
    private ServiceMgtMapper serviceMgtMapper;

    @Override
    public List<ServiceInfo> queryServiceList(String serviceName) {
        String userId = (String)Objects.requireNonNull(RequestContextHolder.getRequestAttributes())
            .getAttribute(EngineConstants.USER_ID, RequestAttributes.SCOPE_REQUEST);
        return serviceMgtMapper.queryServiceList(userId, StringUtils.trim(serviceName));
    }

    @Override
    public List<ServiceNameDTO> queryServiceNameList(String serviceName) {
        String userId = (String)Objects.requireNonNull(RequestContextHolder.getRequestAttributes())
            .getAttribute(EngineConstants.USER_ID, RequestAttributes.SCOPE_REQUEST);
        return serviceMgtMapper.queryServiceNameList(userId, StringUtils.trim(serviceName));
    }

    @Override
    public ServiceInfo queryServiceInfoById(String serviceId) {
        return serviceMgtMapper.queryServiceInfoById(serviceId);
    }

    /**
     * 新增服务信息
     *
     * @param servicesReq
     *            服务表
     */
    @Override
    public void insertService(ServicesReq servicesReq) {
        ServiceInfo serviceInfo = new ServiceInfo();
        BeanUtils.copyProperties(servicesReq, serviceInfo);
        serviceMgtMapper.insertService(serviceInfo);
    }

    /**
     * 修改服务信息
     *
     * @param servicesReq
     *            服务表
     */
    @Override
    public void updateService(ServicesReq servicesReq) {
        ServiceInfo serviceInfo = new ServiceInfo();
        BeanUtils.copyProperties(servicesReq, serviceInfo);
        serviceMgtMapper.updateService(serviceInfo);
    }

    /**
     * 删除服务信息
     *
     * @param serviceInfo
     *            服务表
     */
    @Override
    public void deleteService(ServiceInfo serviceInfo) {
        // serviceMgtMapper.deleteService(serviceInfo);
    }

    /**
     * 获取服务根据id
     *
     * @param serviceId
     *            服务id
     */
    @Override
    public ServiceInfo getServiceById(String serviceId) {
        return serviceMgtMapper.getServiceById(serviceId);
    }

}
