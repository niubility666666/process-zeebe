package com.rt.engine.service;

import java.util.List;

import com.rt.engine.bean.dto.ServiceNameDTO;
import com.rt.engine.bean.entity.ServiceInfo;
import com.rt.engine.bean.request.ServicesReq;

public interface ServiceMgtService {
    /**
     * 查询服务列表
     * 
     * @param serviceName
     *            服务名称
     * @return 服务列表
     */
    List<ServiceInfo> queryServiceList(String serviceName);

    /**
     * 查询服务名称列表
     * 
     * @param serviceName
     *            服务名称
     * @return 服务名称列表
     */
    List<ServiceNameDTO> queryServiceNameList(String serviceName);

    /**
     * 查询服务信息
     * 
     * @param serviceId
     *            服务ID
     * @return 服务信息
     */
    ServiceInfo queryServiceInfoById(String serviceId);

    /**
     * 新增服务信息
     * 
     * @param servicesReq
     *            服务表
     */
    void insertService(ServicesReq servicesReq);

    /**
     * 修改服务信息
     *
     * @param servicesReq
     *            服务表
     */
    void updateService(ServicesReq servicesReq);

    /**
     * 删除服务信息
     *
     * @param serviceInfo
     *            服务表
     */
    void deleteService(ServiceInfo serviceInfo);

    /**
     * 获取服务根据id
     * 
     * @param serviceId
     *            服务id
     */
    ServiceInfo getServiceById(String serviceId);
}
