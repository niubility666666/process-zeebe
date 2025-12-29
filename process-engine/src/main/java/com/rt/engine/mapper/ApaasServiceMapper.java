package com.rt.engine.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.rt.engine.bean.entity.ServiceInfo;

@Repository
public interface ApaasServiceMapper {
    /**
     * 查询apaas服务信息
     * 
     * @return apaas服务信息集合
     */
    @DS("db2")
    List<ServiceInfo> queryApaasServiceList();
}
