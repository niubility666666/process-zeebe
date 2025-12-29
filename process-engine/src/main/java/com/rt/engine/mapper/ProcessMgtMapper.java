package com.rt.engine.mapper;

import java.util.List;

import com.rt.engine.bean.dto.*;
import com.rt.engine.bean.query.ZeebeProcessInstanceQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.rt.engine.bean.entity.ProcessElement;
import com.rt.engine.bean.entity.ProcessInfo;
import com.rt.engine.bean.entity.ProcessInfoHis;
import com.rt.engine.bean.entity.ProcessNodeItem;
import com.rt.engine.bean.entity.RoleInfo;
import com.rt.engine.bean.entity.UserInfo;

@Repository
public interface ProcessMgtMapper {

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
     * @return List
     */
    List<ZeebeProcessInstance> queryProcessTotalList(ProcessInfo processInfo);

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
     * 新增 process
     *
     * @param processInfo
     *            流程信息
     */
    void insertProcessInfo(ProcessInfo processInfo);

    /**
     * 更新 process
     *
     * @param processInfo
     *            流程信息
     */
    void updateProcessInfo(ProcessInfo processInfo);

    /**
     * 取消发布
     *
     * @param processId
     *            表单主键id
     * @param userId
     *            用户id
     * @return Integer
     */
    Integer cancelPublishProcess(@Param("processId") String processId, @Param("userId") String userId);

    /**
     * 查询流程
     *
     * @param processId
     *            流程信息表主键id
     * @return ProcessInfoToDeploymentDto
     */
    ProcessInfo findProcessInfoById(String processId);

    /**
     * 查询流程
     *
     * @param processName
     *            模板名称
     * @return ProcessInfoToDeploymentDto
     */
    List<ProcessInfo> findProcessInfoByName(String processName);

    /**
     * 查询流程
     *
     * @param list
     *            流程信息表主键id集合
     * @return ProcessInfoToDeploymentDto
     */
    List<ProcessInfo> findProcessInfoByIdList(List<String> list);

    /**
     * 删除流程
     *
     * @param list
     *            流程信息表主键id
     * @param userId
     *            用户id
     */
    void deleteProcess(@Param("list") List<String> list, @Param("userId") String userId);

    /**
     * 保存流程发布历史数据
     *
     * @param processInfoHis
     *            发布数据
     */
    void insertProcessInfoHis(ProcessInfoHis processInfoHis);

    /**
     * 保存流程节点信息
     *
     * @param processElementList
     *            流程节点
     */
    void insertProcessElement(List<ProcessElement> processElementList);

    /**
     * 查询流程节点信息
     *
     * @param list
     *            流程idlist
     * @return List
     */
    List<ProcessNodeItem> findProcessNodeItemListByProcessIdList(List<String> list);

    /**
     * 查询用户列表
     * 
     * @param userName
     *            用户名称
     * @return 用户列表
     */
    List<UserInfo> findUserList(String userName);

    /**
     * 查询角色列表
     *
     * @param roleName
     *            角色名称
     * @return 角色列表
     */
    List<RoleInfo> findRoleList(String roleName);

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
     *            表单id
     * @return map
     */
    List<FormColumnDto> queryFormColumnList(String formId);
}
