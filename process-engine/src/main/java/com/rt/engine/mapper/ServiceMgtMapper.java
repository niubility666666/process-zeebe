package com.rt.engine.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rt.engine.bean.dto.ServiceNameDTO;
import com.rt.engine.bean.entity.ServiceInfo;

@Repository
public interface ServiceMgtMapper {
    /**
     * 查询服务列表
     * 
     * @param userId
     *            用户ID
     * @param serviceName
     *            服务名称
     * @return 服务列表
     */
    List<ServiceInfo> queryServiceList(String userId, String serviceName);

    /**
     * 查询服务名称列表
     * 
     * @param userId
     *            用户ID
     * @param serviceName
     *            服务名称
     * @return 服务名称列表
     */
    List<ServiceNameDTO> queryServiceNameList(String userId, String serviceName);

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
     * @param serviceInfo
     *            服务信息
     */
    void insertService(ServiceInfo serviceInfo);

    /**
     * 批量新增服务信息
     * 
     * @param serviceInfoList
     *            服务信息列表
     */
    void batchInsertService(List<ServiceInfo> serviceInfoList);

    /**
     * 删除apaas服务
     */
    void deleteApaasService();

    /**
     * 修改服务信息
     *
     * @param serviceInfo
     *            服务信息
     */
    void updateService(ServiceInfo serviceInfo);

    /**
     * 
     * @param idList
     *            服务ID集合
     */
    void deleteServiceByIds(List<Integer> idList);

    /**
     * 获取服务根据id
     *
     * @param serviceId
     *            服务id
     */
    ServiceInfo getServiceById(String serviceId);
}
