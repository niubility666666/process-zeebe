package com.rt.engine.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.rt.engine.bean.dto.CompleteTaskDto;
import com.rt.engine.bean.dto.CreateInstanceDto;
import com.rt.engine.bean.dto.MouldDto;
import com.rt.engine.bean.dto.ProcessInstanceStatusDto;
import com.rt.engine.bean.dto.TaskDto;
import com.rt.engine.bean.entity.ProcessInfo;
import com.rt.engine.bean.response.BaseResponse;

public interface ProcessApiService {

    /**
     * 创建实例
     * 
     * @param bpmnProcessId
     *            流程ID
     * @param jsonObject
     *            流程参数
     * @return 实例ID和下个任务节点信息
     */
    CreateInstanceDto createInstance(String bpmnProcessId, JSONObject jsonObject);

    /**
     * 取消流程实例
     * 
     * @param processInstanceKey
     *            流程实例id
     * @return 操作结果
     */
    BaseResponse cancelInstance(long processInstanceKey);

    /**
     * 流程节点执行
     * 
     * @param processInstanceKey
     *            流程实例ID
     * @param elementId
     *            节点ID
     * @param action
     *            执行动作
     * @return 执行结果
     */
    CompleteTaskDto completeTask(long processInstanceKey, String elementId, String action);

    /**
     * 分页查询流程列表信息
     *
     * @param processInfo
     *            流程查询对象
     * @return List
     */
    List<MouldDto> queryProcessList(ProcessInfo processInfo);

    /**
     * 查询实例状态
     *
     * @param processInstanceKey
     *            流程实例id
     * @return ProcessInstanceStatusDto
     */
    ProcessInstanceStatusDto queryInstanceStatus(long processInstanceKey);

    /**
     * 查询当前流程实例的下一任务节点数组
     * 
     * @param processInstanceKey
     *            流程实例ID
     * @return 下一任务节点数组
     */
    List<TaskDto> queryNextTaskList(long processInstanceKey);

}
