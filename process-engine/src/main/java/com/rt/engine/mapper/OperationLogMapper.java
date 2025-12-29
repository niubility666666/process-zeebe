package com.rt.engine.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rt.engine.bean.entity.OperationLog;

@Repository
public interface OperationLogMapper {
    /**
     * 记录操作日志
     *
     * @param operationLog
     *            操作日志对象
     */
    void insertOperationLogEntity(OperationLog operationLog);

    /**
     * 查询操作日志列表
     * 
     * @return
     */
    List<OperationLog> queryOperationLogList();
}
