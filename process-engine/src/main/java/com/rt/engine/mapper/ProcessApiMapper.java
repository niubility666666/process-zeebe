package com.rt.engine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.rt.engine.bean.dto.JobInfoDto;
import com.rt.engine.bean.dto.ZeebeElementInstance;
import com.rt.engine.bean.dto.ZeebeProcessInstance;

@Repository
public interface ProcessApiMapper {

    /**
     * 查询部署实例总信息
     *
     * @param processInstanceKey
     *            流程id
     * @return ZeebeProcessInstance
     */
    ZeebeProcessInstance findProcessInstanceByKey(@Param("key") long processInstanceKey);

    /**
     * 查询流程中的相关信息
     *
     * @param instanceId
     *            流程实例id
     * @return List
     */
    List<ZeebeElementInstance> findElementInstanceByProcessId(String instanceId);

    /**
     * 查询当前流程实例的待处理任务信息
     *
     * @param processInstanceKey
     *            流程实例id
     * @return 待处理任务
     */
    List<JobInfoDto> findNextJobsByInstanceKey(long processInstanceKey);
}
