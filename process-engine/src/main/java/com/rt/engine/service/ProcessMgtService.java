package com.rt.engine.service;

import java.util.List;

import com.rt.engine.bean.dto.*;
import com.rt.engine.bean.entity.ProcessInfo;
import com.rt.engine.bean.entity.RoleInfo;
import com.rt.engine.bean.entity.UserInfo;
import com.rt.engine.bean.query.ZeebeProcessInstanceQuery;
import com.rt.engine.bean.request.SaveProcessInfoReq;
import com.rt.engine.bean.response.BaseResponse;
import com.rt.engine.bean.response.ResponseVO;

public interface ProcessMgtService {

    /**
     * 分页查询流程列表信息
     *
     * @param processInfo
     *            流程查询对象
     * @return List
     */
    List<ProcessInfoDto> queryProcessList(ProcessInfo processInfo);

    /**
     * 分页查询流程监控列表信息
     *
     * @param processInfo
     *            流程查询对象
     * @return List
     */
    List<ProcessForMonitorDto> queryProcessForMonitorList(ProcessInfo processInfo);

    /**
     * 统计流程监控列表信息
     *
     * @param processInfo
     *            流程查询对象
     * @return ProcessForTotalDto
     */
    ProcessForTotalDto queryProcessTotalList(ProcessInfo processInfo);

    /**
     * 统计当前流程下所有实例信息
     *
     * @param zeebeProcessInstanceQuery
     *            流程查询对象
     * @return ProcessForTotalDto
     */
    List<ZeebeProcessInstanceDto> queryInstanceList(ZeebeProcessInstanceQuery zeebeProcessInstanceQuery);

    /**
     * 统计当前流程下所有实例信息
     *
     * @param instanceId
     *            实例id
     * @return ProcessInstanceViewDto
     */
    List<ProcessInstanceViewDto> queryInstanceDetail(Long instanceId);

    /**
     * 保存bpmn
     *
     * @param saveProcessInfoReq
     *            请求参数
     * @return 操作结果
     */
    BaseResponse saveProcessInfo(SaveProcessInfoReq saveProcessInfoReq);

    /**
     * 修改bpmn
     *
     * @param saveProcessInfoReq
     *            请求参数
     * @return 操作结果
     */
    BaseResponse modifyProcessInfo(SaveProcessInfoReq saveProcessInfoReq);

    /**
     * 查询流程详情信息
     *
     * @param processId
     *            流程ID
     * @return 返回详情
     */
    ResponseVO queryProcessDetail(String processId);

    /**
     * 流程发布
     *
     * @param processId
     *            流程定义id
     * @param userId
     *            用户id
     * @return 发布结果
     */
    BaseResponse publishProcess(String processId, String userId);

    /**
     * 删除流程
     *
     * @param list
     *            流程信息表主键id集合
     * @param userId
     *            用户id
     * @return BaseResponse
     */
    BaseResponse deleteProcess(List<String> list, String userId);

    /**
     * 取消发布
     *
     * @param processId
     *            流程信息表主键id
     * @param userId
     *            用户id
     * @return BaseResponse
     */
    BaseResponse cancelPublishProcess(String processId, String userId);

    /**
     * 视图查询接口
     *
     * @param formName
     *            表单名字
     * @return map
     */
    List<FormDto> queryFormList(String formName);

    /**
     * 视图查询接口
     *
     * @param formId
     *            表单字段名字
     * @return map
     */
    List<FormColumnDto> queryFormColumnList(String formId);

    /**
     * 查询用户列表
     *
     * @param userName
     *            用户名称
     * @return 用户列表
     */
    List<UserInfo> queryUserList(String userName);

    /**
     * 查询角色列表
     *
     * @param roleName
     *            角色名称
     * @return 角色列表
     */
    List<RoleInfo> queryRoleList(String roleName);
}
