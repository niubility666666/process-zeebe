package com.rt.engine.service.impl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.rt.engine.bean.response.BaseResponse;
import com.rt.engine.bean.response.ResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.rt.engine.bean.dto.CompleteTaskDto;
import com.rt.engine.bean.dto.CreateInstanceDto;
import com.rt.engine.bean.dto.JobInfoDto;
import com.rt.engine.bean.dto.MouldDto;
import com.rt.engine.bean.dto.ProcessInfoDto;
import com.rt.engine.bean.dto.ProcessInstanceStatusDto;
import com.rt.engine.bean.dto.TaskActionDto;
import com.rt.engine.bean.dto.TaskDto;
import com.rt.engine.bean.dto.TaskLog;
import com.rt.engine.bean.dto.ZeebeProcessInstance;
import com.rt.engine.bean.entity.ProcessInfo;
import com.rt.engine.bean.entity.ProcessNodeItem;
import com.rt.engine.bean.entity.ProcessVariable;
import com.rt.engine.common.constants.CodeEnum;
import com.rt.engine.common.constants.ElementType;
import com.rt.engine.common.constants.EngineConstants;
import com.rt.engine.common.constants.ItemTypeEnum;
import com.rt.engine.common.constants.ItemTypeOperation;
import com.rt.engine.common.constants.SymbolConstants;
import com.rt.engine.common.exception.ProcessEngineException;
import com.rt.engine.common.reg.RegClass;
import com.rt.engine.common.util.TimeUtil;
import com.rt.engine.mapper.ProcessApiMapper;
import com.rt.engine.mapper.ProcessMgtMapper;
import com.rt.engine.mapper.ProcessNodeItemMapper;
import com.rt.engine.mapper.ProcessVariableDao;
import com.rt.engine.service.ProcessApiService;
import com.google.common.collect.Lists;

import io.camunda.zeebe.client.api.command.CreateProcessInstanceCommandStep1;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.spring.client.ZeebeClientLifecycle;

@Service
public class ProcessApiServiceImpl implements ProcessApiService {

    @Resource
    private ZeebeClientLifecycle zeebeClientLifecycle;

    @Resource
    private ProcessApiMapper processApiMapper;

    @Resource
    private ProcessMgtMapper processMgtMapper;

    @Resource
    private ProcessNodeItemMapper processNodeItemMapper;

    @Resource
    private ProcessVariableDao processVariableDao;

    @Override
    public CreateInstanceDto createInstance(String bpmnProcessId, JSONObject jsonObject) {
        // 根据bpmnProcessId 查询流程状态是否发布
        ProcessInfo processInfo = processMgtMapper.findProcessInfoById(bpmnProcessId);
        if (EngineConstants.RELEASE_ZERO.intValue() == processInfo.getIsReleased().intValue()) {
            throw new ProcessEngineException(CodeEnum.PROCESS_NOT_RELEASE);
        }
        CreateProcessInstanceCommandStep1.CreateProcessInstanceCommandStep3 commandStep3 =
            zeebeClientLifecycle.newCreateInstanceCommand().bpmnProcessId(bpmnProcessId).latestVersion();


        // 设置流程变量
        if (jsonObject != null && !jsonObject.isEmpty()) {
            commandStep3.variables(jsonObject);
        }
        ProcessInstanceEvent processInstanceEvent = commandStep3.send().join();

        long processInstanceKey = processInstanceEvent.getProcessInstanceKey();
        // zeebe 异步处理等待
        TimeUtil.sleep(50);

        // 用户任务第一个节点需要自己走掉
        List<JobInfoDto> jobInfoDtoList = processApiMapper.findNextJobsByInstanceKey(processInstanceKey);
        jobInfoDtoList.forEach(jobInfoDto -> {
            if (ElementType.USER_TASK.equals(jobInfoDto.getElementType())) {
                zeebeClientLifecycle.newCompleteCommand(jobInfoDto.getJobKey()).send().join();
                // zeebe 异步处理等待
                TimeUtil.sleep(100);
                // 保存流程变量
                processVariableDao.insertProcessVariable(
                    ProcessVariable.builder().jobKey(jobInfoDto.getJobKey()).processId(jobInfoDto.getBpmnProcessId())
                        .processInstanceKey(jobInfoDto.getProcessInstanceKey()).elementId(jobInfoDto.getElementId())
                        .elementInstanceKey(jobInfoDto.getElementInstanceKey()).elementName(jobInfoDto.getElementName())
                        .elementStartTime(jobInfoDto.getStartTime()).variableName(EngineConstants.VARIABLE_APPROVE)
                        .variableValue(ItemTypeOperation.AGREE.name().toLowerCase(Locale.ROOT)).build());
            }
        });
        return CreateInstanceDto.builder().processInstanceKey(processInstanceKey)
            .taskList(this.queryNextTaskList(processInstanceKey)).build();
    }

    @Override
    public BaseResponse cancelInstance(long processInstanceKey) {
        // 查询运行实例的总信息
        ZeebeProcessInstance zeebeProcessInstance = processApiMapper.findProcessInstanceByKey(processInstanceKey);
        zeebeClientLifecycle.newCancelInstanceCommand(processInstanceKey).send().cancel(Boolean.TRUE);
        // 如果已完成
        if (StringUtils.equalsIgnoreCase(EngineConstants.INSTANCE_STATE_COMPLETED, zeebeProcessInstance.getState())) {
            return ResponseVO.fail(CodeEnum.TASK_STATE_COMPLETED);
        } else if (StringUtils.equalsIgnoreCase(EngineConstants.INSTANCE_STATE_TERMINATED,
            zeebeProcessInstance.getState())) {
            return ResponseVO.fail(CodeEnum.TASK_STATE_TERMINATED);
        }
        return BaseResponse.success();
    }

    @Override
    public CompleteTaskDto completeTask(long processInstanceKey, String elementId, String action) {
        // 参数枚举校验
        if (!(ItemTypeOperation.AGREE.name().equalsIgnoreCase(action)
            || ItemTypeOperation.REJECT.name().equalsIgnoreCase(action)
            || ItemTypeOperation.FALLBACK.name().equalsIgnoreCase(action))) {
            throw new ProcessEngineException(CodeEnum.PARAMETER_ERROR);
        }
        // 查询出当前实例版本下拥有的权限信息
        List<ProcessNodeItem> processNodeItemList =
            processNodeItemMapper.findProcessNodeItemListByInstanceKey(processInstanceKey);
        // 验证是否拥有权限
        verifyAuthority(processNodeItemList, elementId, action);
        // 流程终止
        if (ItemTypeOperation.REJECT.name().equalsIgnoreCase(action)) {
            zeebeClientLifecycle.newCancelInstanceCommand(processInstanceKey).send().cancel(Boolean.TRUE);
        }
        // 根据流程实例ID查询流程任务信息
        List<JobInfoDto> jobInfoDtoList = processApiMapper.findNextJobsByInstanceKey(processInstanceKey);
        JobInfoDto jobInfoDto = jobInfoDtoList.stream().filter(job -> elementId.equals(job.getElementId())).findFirst()
            .orElseThrow(() -> new ProcessEngineException(CodeEnum.JOB_NOT_EXISTS));
        // 同意或退回
        if (ItemTypeOperation.AGREE.name().equalsIgnoreCase(action)
            || ItemTypeOperation.FALLBACK.name().equalsIgnoreCase(action)) {
            // 设置流程节点变量
            Map<String, String> variables = new HashMap<>(20);
            variables.put(EngineConstants.VARIABLE_APPROVE, action);
            // 流程扭转
            zeebeClientLifecycle.newCompleteCommand(jobInfoDto.getJobKey()).variables(variables).send().join();
        }
        // 筛选出到底是退回上一步还是退回制定节点
        String variableValue = action;
        if (ItemTypeOperation.FALLBACK.name().equalsIgnoreCase(action)) {
            ProcessNodeItem processNodeItem = processNodeItemList.stream()
                .filter(x -> elementId.equals(x.getElementId())
                    && StringUtils.equals(ItemTypeEnum.ITEM_TYPE_OPERATION.getId(), x.getItemType()))
                .findFirst().orElse(new ProcessNodeItem());
            if (Arrays.asList(StringUtils.split(processNodeItem.getItemValue(), SymbolConstants.COMMA_EN))
                .contains(ItemTypeOperation.FALLBACK.getId())) {
                variableValue = ItemTypeOperation.FALLBACK.name().toLowerCase(Locale.ROOT);
            } else if (Arrays.asList(StringUtils.split(processNodeItem.getItemValue(), SymbolConstants.COMMA_EN))
                .contains(ItemTypeOperation.FALLBACK_NODE.getId())) {
                variableValue = ItemTypeOperation.FALLBACK_NODE.name().toLowerCase(Locale.ROOT);
            }
        }
        // 保存流程变量
        processVariableDao.insertProcessVariable(ProcessVariable.builder().jobKey(jobInfoDto.getJobKey())
            .processId(jobInfoDto.getBpmnProcessId()).processInstanceKey(jobInfoDto.getProcessInstanceKey())
            .elementId(jobInfoDto.getElementId()).elementInstanceKey(jobInfoDto.getElementInstanceKey())
            .elementName(jobInfoDto.getElementName()).elementStartTime(jobInfoDto.getStartTime())
            .variableName(EngineConstants.VARIABLE_APPROVE).variableValue(variableValue).build());
        // zeebe 异步处理等待
        TimeUtil.sleep(100);
        // 查询运行实例的总信息
        ZeebeProcessInstance zeebeProcessInstance = processApiMapper.findProcessInstanceByKey(processInstanceKey);

        return CompleteTaskDto.builder()
            .isOver(EngineConstants.INSTANCE_STATE_COMPLETED.equalsIgnoreCase(zeebeProcessInstance.getState())
                || EngineConstants.INSTANCE_STATE_TERMINATED.equalsIgnoreCase(zeebeProcessInstance.getState()))
            .taskList(this.queryNextTaskList(processInstanceKey)).build();
    }

    /**
     * 验证当前实例下的节点是否拥有action权限
     * 
     * @param processNodeItemList
     *            当前流程配置的节点信息
     * @param elementId
     *            节点id
     * @param action
     *            动作(agree, reject, fallback)
     */
    private void verifyAuthority(List<ProcessNodeItem> processNodeItemList, String elementId, String action) {
        // 筛选出当前节点的权限信息
        ProcessNodeItem processNodeItem = processNodeItemList.stream()
            .filter(x -> StringUtils.equals(x.getElementId(), elementId)
                && StringUtils.equals(ItemTypeEnum.ITEM_TYPE_OPERATION.getId(), x.getItemType()))
            .findFirst().orElse(null);
        if (processNodeItem != null) {
            // 筛选出当前动作的枚举信息
            ItemTypeOperation itemTypeOperation = Arrays.stream(ItemTypeOperation.values())
                .filter(y -> StringUtils.equalsAnyIgnoreCase(action, y.name())).findFirst().orElseThrow(() -> {
                    throw new ProcessEngineException(CodeEnum.AUTHORITY_NOT_DIS.getCode(),
                        String.format(CodeEnum.AUTHORITY_NOT_DIS.getDesc(), action));
                });
            // 判断是否拥有权限
            List<String> stringList =
                Lists.newArrayList(processNodeItem.getItemValue().split(SymbolConstants.COMMA_EN));
            if (stringList.contains(ItemTypeOperation.FALLBACK.getId())
                || stringList.contains(ItemTypeOperation.FALLBACK_NODE.getId())) {
                stringList
                    .addAll(Arrays.asList(ItemTypeOperation.FALLBACK.getId(), ItemTypeOperation.FALLBACK_NODE.getId()));
                stringList = stringList.stream().distinct().collect(Collectors.toList());
            }
            if (!stringList.contains(itemTypeOperation.getId())) {
                throw new ProcessEngineException(CodeEnum.AUTHORITY_NOT.getCode(),
                    String.format(CodeEnum.AUTHORITY_NOT.getDesc(), action, itemTypeOperation.getName()));
            }
        }
    }

    @Override
    public List<MouldDto> queryProcessList(ProcessInfo processInfo) {
        List<ProcessInfoDto> processInfoList = processMgtMapper.queryProcessList(processInfo);
        // 查询出当前流程中包含得所有节点信息，不区分版本
        List<ProcessNodeItem> nodeItemList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(processInfoList)) {
            nodeItemList = processMgtMapper.findProcessNodeItemListByProcessIdList(
                processInfoList.stream().map(ProcessInfoDto::getProcessId).collect(Collectors.toList()));
        }
        List<MouldDto> mouldDtoList = Lists.newArrayList();
        List<ProcessNodeItem> finalNodeItemList = nodeItemList;
        processInfoList.forEach(x -> {
            // 构建基础得模板信息
            MouldDto mouldDto = MouldDto.builder().processId(x.getProcessId()).processName(x.getProcessName())
                .version(x.getVersion()).updateTime(x.getUpdatedTime()).bpmnXml(x.getBpmnXml()).build();
            // 筛选出符合条件得当前流程当前版本下得节点信息
            List<String> stringList = Lists.newArrayList();
            finalNodeItemList.stream().filter(y -> StringUtils.equalsIgnoreCase(x.getProcessId(), y.getProcessId())
                && x.getVersion().equals(y.getVersion())).forEach(k -> {
                    Matcher matcher = RegClass.getBracePattern(k.getItemValue());
                    while (matcher.find()) {
                        String group = matcher.group();
                        stringList.add(StringUtils.substring(group, 1, group.length() - 1));
                    }
                });
            mouldDto.setParameter(stringList.stream().distinct().collect(Collectors.toList()));
            mouldDtoList.add(mouldDto);
        });
        return mouldDtoList;
    }

    /**
     * 查询实例状态
     *
     * @param processInstanceKey
     *            流程实例id
     * @return ProcessInstanceStatusDto
     */
    @Override
    public ProcessInstanceStatusDto queryInstanceStatus(long processInstanceKey) {
        // 查询运行实例的总信息
        ZeebeProcessInstance zeebeProcessInstance = processApiMapper.findProcessInstanceByKey(processInstanceKey);
        // 构建基础的流程实例信息
        ProcessInstanceStatusDto processInstanceStatusDto =
            ProcessInstanceStatusDto.builder().instanceId(processInstanceKey).isOver(false).build();
        // 如果已完成
        if (Arrays.asList(EngineConstants.INSTANCE_STATE_COMPLETED, EngineConstants.INSTANCE_STATE_TERMINATED)
            .contains(zeebeProcessInstance.getState())) {
            processInstanceStatusDto.setOver(true);
        }
        // 拼接模板信息
        ProcessInfo processInfo = processMgtMapper.findProcessInfoById(zeebeProcessInstance.getBpmnProcessId());
        processInstanceStatusDto.setVersion(processInfo.getVersion().toString());
        // 根据流程id查询出流程详细记录
        List<ProcessVariable> processVariableList =
            processVariableDao.findTaskLogList(processInstanceKey, zeebeProcessInstance.getBpmnProcessId());
        // 查询出已处理任务的处理类型相关的
        List<ProcessNodeItem> processNodeItemList =
            processNodeItemMapper.findAlreadyProcessNodeItemList(processInfo.getProcessId(), processInfo.getVersion());
        // 已处理任务记录插入类型和类型名称以及处理结果
        List<TaskLog> newTaskLogList = Lists.newArrayList();
        processVariableList.forEach(x -> {
            TaskLog taskLog = new TaskLog();
            BeanUtils.copyProperties(x, taskLog);
            taskLog.setStartTime(x.getElementStartTime());
            taskLog.setEndTime(x.getElementEndTime());
            newTaskLogList.add(taskLog);
            ItemTypeOperation itemTypeOperation = Arrays.stream(ItemTypeOperation.values())
                .filter(y -> StringUtils.equalsAnyIgnoreCase(x.getVariableValue(), y.name())).findFirst().orElse(null);
            taskLog.setDoTaskResult(Optional.ofNullable(itemTypeOperation).map(ItemTypeOperation::getName).orElse(null));
            processNodeItemList.stream().filter(y -> StringUtils.equalsIgnoreCase(x.getElementId(), y.getElementId()))
                .findFirst().ifPresent(k -> {
                    taskLog.setToTreatType(k.getItemType());
                    taskLog.setToTreat(Arrays.asList(k.getItemValue().split(SymbolConstants.COMMA_EN)));
                });
        });
        // 拼装一条完整的流程起始结束相关信息数据
        processInstanceStatusDto.setTaskLogList(newTaskLogList);
        // 调用插入task信息公共方法
        processInstanceStatusDto.setNodeTaskList(queryNextTaskList(processInstanceKey));
        return processInstanceStatusDto;
    }

    @Override
    public List<TaskDto> queryNextTaskList(long processInstanceKey) {
        List<JobInfoDto> jobInfoDtoList = processApiMapper.findNextJobsByInstanceKey(processInstanceKey);
        List<TaskDto> taskDtoList = new ArrayList<>();
        jobInfoDtoList.forEach(jobInfoDto -> {
            List<ProcessNodeItem> processNodeItemList = processNodeItemMapper
                .findProcessNodeItemList(ProcessNodeItem.builder().processId(jobInfoDto.getBpmnProcessId())
                    .elementId(jobInfoDto.getElementId()).version(jobInfoDto.getVersion()).build());
            TaskDto taskDto =
                TaskDto.builder().elementId(jobInfoDto.getElementId()).elementName(jobInfoDto.getElementName()).build();
            for (ProcessNodeItem processNodeItem : processNodeItemList) {
                if (ItemTypeEnum.ITEM_TYPE_USER.getId().equals(processNodeItem.getItemType())
                    || ItemTypeEnum.ITEM_TYPE_ROLE.getId().equals(processNodeItem.getItemType())) {
                    taskDto.setToTreatType(processNodeItem.getItemType());
                    taskDto.setToTreat(Arrays.asList(processNodeItem.getItemValue().split(SymbolConstants.COMMA_EN)));
                }
                // 获取节点的操作权限
                if (ItemTypeEnum.ITEM_TYPE_OPERATION.getId().equals(processNodeItem.getItemType())) {
                    TaskActionDto taskActionDto = new TaskActionDto();
                    if (StringUtils.isNotBlank(processNodeItem.getItemValue())) {
                        String[] operationArray = processNodeItem.getItemValue().split(SymbolConstants.COMMA_EN);
                        for (String operation : operationArray) {
                            if (ItemTypeOperation.AGREE.getId().equals(operation)) {
                                taskActionDto.setAgree(true);
                            }
                            if (ItemTypeOperation.REJECT.getId().equals(operation)) {
                                taskActionDto.setReject(true);
                            }
                            if (ItemTypeOperation.FALLBACK.getId().equals(operation)
                                || ItemTypeOperation.FALLBACK_NODE.getId().equals(operation)) {
                                taskActionDto.setFallback(true);
                                taskActionDto.setFallbackIds(
                                    Arrays.asList(processNodeItem.getExt1().split(SymbolConstants.COMMA_EN)));
                            }
                        }
                    }
                    taskDto.setAction(taskActionDto);
                }
            }
            taskDtoList.add(taskDto);
        });
        return taskDtoList;
    }
}
