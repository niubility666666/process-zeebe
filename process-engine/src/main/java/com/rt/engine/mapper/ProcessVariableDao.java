package com.rt.engine.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.rt.engine.bean.entity.ProcessVariable;

import java.util.List;

@Repository
public interface ProcessVariableDao {
    /**
     * 保存流程变量
     * 
     * @param processVariable
     *            流程变量
     */
    void insertProcessVariable(ProcessVariable processVariable);

    /**
     * 查询已处理任务记录
     *
     * @param instanceId
     *            实例id
     * @param bpmnProcessId
     *            流程id
     * @return List
     */
    List<ProcessVariable> findTaskLogList(@Param("instanceId") long instanceId,
        @Param("bpmnProcessId") String bpmnProcessId);
}
