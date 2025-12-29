package com.rt.engine.service.impl;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.rt.engine.bean.dto.*;
import com.rt.engine.bean.entity.*;
import com.rt.engine.bean.dto.*;
import com.rt.engine.bean.entity.*;
import com.rt.engine.bean.query.ZeebeProcessInstanceQuery;
import com.rt.engine.common.constants.*;
import com.rt.engine.common.constants.*;
import com.rt.engine.mapper.ProcessParamVariableMapper;
import io.camunda.zeebe.model.bpmn.instance.*;
import io.camunda.zeebe.model.bpmn.instance.Process;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.rt.engine.bean.request.SaveProcessInfoReq;
import com.rt.engine.bean.response.BaseResponse;
import com.rt.engine.bean.response.ResponseVO;
import com.rt.engine.common.exception.ProcessEngineException;
import com.rt.engine.mapper.ProcessMgtMapper;
import com.rt.engine.mapper.ProcessNodeItemMapper;
import com.rt.engine.service.ProcessMgtService;
import com.google.common.collect.Lists;

import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import io.camunda.zeebe.spring.client.ZeebeClientLifecycle;

@Service
public class ProcessMgtServiceImpl implements ProcessMgtService {
    private Logger logger = LoggerFactory.getLogger(ProcessMgtServiceImpl.class);

    @Resource
    private ZeebeClientLifecycle zeebeClientLifecycle;
    @Resource
    private ProcessMgtMapper processMgtMapper;
    @Resource
    private ProcessNodeItemMapper processNodeItemMapper;

    @Resource
    private ProcessParamVariableMapper processParamVariableMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse saveProcessInfo(SaveProcessInfoReq saveProcessInfoReq) {
        final BpmnModelInstance bpmnModelInstance =
            ProcessMgtServiceUtil.getBpmnModelInstanceFromXml(saveProcessInfoReq.getBpmnXml());

        Process process = bpmnModelInstance.getModelElementsByType(Process.class).stream().findFirst()
            .orElseThrow(() -> new ProcessEngineException(CodeEnum.BPMN_FORMAT_EXCEPTION));

        // 判断processId是否存在
        ProcessInfo processInfo = processMgtMapper.findProcessInfoById(process.getId());
        if (processInfo != null) {
            throw new ProcessEngineException(CodeEnum.PROCESS_EXISTS);
        }
        // 验证是否重复
        verityProcessExists(process, true);
        // 验证task节点是否支持
        verityTaskType(bpmnModelInstance);
        if (StringUtils.isBlank(process.getName())) {
            throw new ProcessEngineException(CodeEnum.PROCESS_NAME_EMPTY);
        }
        processInfo = ProcessInfo.builder().processId(process.getId()).processName(process.getName())
            .formId(saveProcessInfoReq.getFormId()).bpmnXml(saveProcessInfoReq.getBpmnXml())
            .createdBy(saveProcessInfoReq.getUserId()).build();
        processMgtMapper.insertProcessInfo(processInfo);

        if (!CollectionUtils.isEmpty(saveProcessInfoReq.getNodeItems())) {
            List<ProcessNodeItem> processNodeItems = ProcessMgtServiceUtil.getProcessNodeItem(bpmnModelInstance,
                processInfo.getProcessId(), saveProcessInfoReq.getNodeItems());
            verityProcessTask(bpmnModelInstance, saveProcessInfoReq.getNodeItems());
            if (!CollectionUtils.isEmpty(processNodeItems)) {
                processNodeItemMapper.batchInsertProcessNodeItem(processNodeItems);
            }
        }

        // serviceTask节点保存
        if (!CollectionUtils.isEmpty(saveProcessInfoReq.getNodeItemForServiceTaskList())) {
            List<ProcessParamVariable> processParamVariableList = new ArrayList<>();
            saveProcessInfoReq.getNodeItemForServiceTaskList().forEach(x -> {
                ProcessParamVariable processParamVariable = new ProcessParamVariable();
                BeanUtils.copyProperties(x, processParamVariable);
            });
            verityProcessTask(bpmnModelInstance, saveProcessInfoReq.getNodeItems());
            if (!CollectionUtils.isEmpty(processParamVariableList)) {
                processParamVariableMapper.batchInsertProcessParamVariable(processParamVariableList);
            }
        }
        return BaseResponse.success();
    }

    /**
     * 判断是否模板名称重复
     * 
     * @param process
     *            流程对象
     * @param flag
     *            状态 true 新增，false修改
     */
    private void verityProcessExists(Process process, boolean flag) {
        List<ProcessInfo> processInfoList = processMgtMapper.findProcessInfoByName(process.getName());
        // 新增验证
        if (!CollectionUtils.isEmpty(processInfoList) && flag) {
            throw new ProcessEngineException(CodeEnum.PROCESS_EXISTS);
        }
        // 修改验证
        if (!flag) {
            for (var processInfo : processInfoList) {
                if (!processInfo.getProcessId().equals(process.getId())
                    && process.getName().equals(processInfo.getProcessName())) {
                    throw new ProcessEngineException(CodeEnum.PROCESS_EXISTS);
                }
            }
        }
    }

    /**
     * 验证是否除了第一个节点，其他节点都配置了人员信息和动作权限
     * 
     * @param bpmnModelInstance
     *            流程实例
     * @param nodeItemList
     *            前端传过来的节点配置信息
     */
    private void verityProcessTask(BpmnModelInstance bpmnModelInstance, List<NodeItem> nodeItemList) {
        List<UserTask> userTaskList = new ArrayList<>(bpmnModelInstance.getModelElementsByType(UserTask.class));
        for (int i = 1; i < userTaskList.size(); i++) {
            // 跳过第一个默认放过的task节点信息
            String userTaskId = userTaskList.get(i).getId();
            NodeItem nodeItem = nodeItemList.stream().filter(x -> x.getElementId().equalsIgnoreCase(userTaskId))
                .findFirst().orElse(new NodeItem());
            boolean b = (CollectionUtils.isEmpty(nodeItem.getUsers()) && CollectionUtils.isEmpty(nodeItem.getRoles()))
                || CollectionUtils.isEmpty(nodeItem.getOperations());
            if (b) {
                throw new ProcessEngineException(CodeEnum.TASK_NOT.getCode(),
                    String.format(CodeEnum.TASK_NOT.getDesc(), nodeItem.getElementId()));
            }
        }
    }

    /**
     * 验证task类型支不支持
     * 
     * @param bpmnModelInstance
     *            流程model对象
     */
    private void verityTaskType(BpmnModelInstance bpmnModelInstance) {
        // 验证是否有不支持的task类型
        Collection<Task> taskCollection = bpmnModelInstance.getModelElementsByType(Task.class);
        for (var task : taskCollection) {
            if (!(task instanceof UserTask) && !(task instanceof ServiceTask)) {
                throw new ProcessEngineException(CodeEnum.TASK_NOT_SUPPORT.getCode(), String
                    .format(CodeEnum.TASK_NOT_SUPPORT.getDesc(), task.getId(), task.getElementType().getTypeName()));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse modifyProcessInfo(SaveProcessInfoReq saveProcessInfoReq) {
        final BpmnModelInstance bpmnModelInstance =
            ProcessMgtServiceUtil.getBpmnModelInstanceFromXml(saveProcessInfoReq.getBpmnXml());

        Process process = bpmnModelInstance.getModelElementsByType(Process.class).stream().findFirst()
            .orElseThrow(() -> new ProcessEngineException(CodeEnum.BPMN_FORMAT_EXCEPTION));
        // 流程模板不存在
        ProcessInfo processInfo = processMgtMapper.findProcessInfoById(process.getId());
        if (processInfo == null) {
            throw new ProcessEngineException(CodeEnum.PROCESS_NOT_EXISTS);
        }
        // 验证模板是否重复
        verityProcessExists(process, false);
        // 验证task节点是否支持
        verityTaskType(bpmnModelInstance);
        ProcessInfo updateProcessInfo = ProcessInfo.builder().processId(process.getId()).processName(process.getName())
            .formId(saveProcessInfoReq.getFormId()).bpmnXml(saveProcessInfoReq.getBpmnXml())
            .updatedBy(saveProcessInfoReq.getUserId()).build();
        processMgtMapper.updateProcessInfo(updateProcessInfo);

        if (!CollectionUtils.isEmpty(saveProcessInfoReq.getNodeItems())) {
            processNodeItemMapper.deleteProcessNodeItemById(updateProcessInfo.getProcessId());
            List<ProcessNodeItem> processNodeItems = ProcessMgtServiceUtil.getProcessNodeItem(bpmnModelInstance,
                processInfo.getProcessId(), saveProcessInfoReq.getNodeItems());
            verityProcessTask(bpmnModelInstance, saveProcessInfoReq.getNodeItems());
            if (!CollectionUtils.isEmpty(processNodeItems)) {
                processNodeItems.forEach(processNodeItem -> {
                    processNodeItem.setVersion(processInfo.getVersion());
                });
                processNodeItemMapper.batchInsertProcessNodeItem(processNodeItems);
            }
        }

        // serviceTask节点保存
        if (!CollectionUtils.isEmpty(saveProcessInfoReq.getNodeItemForServiceTaskList())) {
            // 先删除节点参数信息
            processParamVariableMapper.deleteProcessParamVariableByProcessId(updateProcessInfo.getProcessId());
            List<ProcessParamVariable> processParamVariableList = new ArrayList<>();
            saveProcessInfoReq.getNodeItemForServiceTaskList().forEach(x -> {
                ProcessParamVariable processParamVariable = new ProcessParamVariable();
                BeanUtils.copyProperties(x, processParamVariable);
            });
            verityProcessTask(bpmnModelInstance, saveProcessInfoReq.getNodeItems());
            if (!CollectionUtils.isEmpty(processParamVariableList)) {
                processParamVariableMapper.batchInsertProcessParamVariable(processParamVariableList);
            }
        }
        return BaseResponse.success();
    }

    @Override
    public ResponseVO queryProcessDetail(String processId) {
        ProcessInfo processInfo = processMgtMapper.findProcessInfoById(processId);
        if (processInfo != null) {
            List<ProcessNodeItem> processNodeItemList = processNodeItemMapper
                .findProcessNodeItemList(ProcessNodeItem.builder().processId(processInfo.getProcessId())
                    .version(processInfo.getVersion()).isReleased(processInfo.getIsReleased()).build());

            List<NodeItem> nodeItemList = ProcessMgtServiceUtil.getNodeItem(processNodeItemList);
            return ResponseVO.success(SaveProcessInfoReq.builder().formId(processInfo.getFormId())
                .bpmnXml(processInfo.getBpmnXml()).nodeItems(nodeItemList).build());
        }
        return ResponseVO.success(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse publishProcess(String processId, String userId) {
        // 流程模板不存在
        ProcessInfo processInfo = processMgtMapper.findProcessInfoById(processId);
        if (processInfo == null) {
            throw new ProcessEngineException(CodeEnum.PROCESS_NOT_EXISTS);
        }
        BpmnModelInstance bpmnModelInstance = Bpmn
            .readModelFromStream(new ByteArrayInputStream(processInfo.getBpmnXml().getBytes(StandardCharsets.UTF_8)));
        // 验证是否有结束节点
        Collection<EndEvent> endEventCollection = bpmnModelInstance.getModelElementsByType(EndEvent.class);
        if (CollectionUtils.isEmpty(endEventCollection)) {
            throw new ProcessEngineException(CodeEnum.PROCESS_END_EVENT_EMPTY.getCode(),
                String.format(CodeEnum.PROCESS_END_EVENT_EMPTY.getDesc(), "结束"));
        }
        // 验证是否有开始节点
        Collection<StartEvent> startEventCollection = bpmnModelInstance.getModelElementsByType(StartEvent.class);
        if (CollectionUtils.isEmpty(startEventCollection)) {
            throw new ProcessEngineException(CodeEnum.PROCESS_END_EVENT_EMPTY.getCode(),
                String.format(CodeEnum.PROCESS_END_EVENT_EMPTY.getDesc(), "开始"));
        }
        List<ProcessNodeItem> processNodeItemList = processNodeItemMapper
            .findProcessNodeItemList(ProcessNodeItem.builder().processId(processInfo.getProcessId())
                .version(processInfo.getVersion()).isReleased(processInfo.getIsReleased()).build());

        // 处理条件表达式
        ProcessMgtServiceUtil.convertConditionExpression(bpmnModelInstance);

        // 针对用户任务类型的退回，需要手动绘制退回节点连线
        processNodeItemList.stream()
            .filter(item -> ElementType.USER_TASK.equals(item.getElementType())
                && ItemTypeEnum.ITEM_TYPE_OPERATION.getId().equals(item.getItemType())
                && (item.getItemValue().contains(ItemTypeOperation.FALLBACK.getId())
                    || item.getItemValue().contains(ItemTypeOperation.FALLBACK_NODE.getId())))
            .forEach(item -> ProcessMgtServiceUtil.addGateWay(bpmnModelInstance, item.getElementId(), item.getExt1()));

        // 流程部署
        DeploymentEvent deploymentEvent = null;
        try {
            deploymentEvent = zeebeClientLifecycle.newDeployCommand()
                .addProcessModel(bpmnModelInstance, processInfo.getProcessName()).send().join();
        } catch (Exception e) {
            logger.error(new SimpleDateFormat(SymbolConstants.TIME_PATTEN).format(new Date())
                + SymbolConstants.COLON_SPACE + e.getMessage());
            String message = e.getMessage();
            if (message.contains(SymbolConstants.ERRORS)) {
                message = StringUtils.substring(message, message.indexOf(SymbolConstants.ERRORS));
            }
            throw new ProcessEngineException(CodeEnum.PARAMETER_ERROR.getCode(), message);
        }

        io.camunda.zeebe.client.api.response.Process processResponse =
            deploymentEvent.getProcesses().stream().findFirst().orElseThrow();

        // 保存 PROCESS_ELEMENT_T
        Collection<Process> processes = bpmnModelInstance.getModelElementsByType(Process.class);
        Process process = processes.stream().findFirst().orElseThrow();
        List<ProcessElement> processElementList = new ArrayList<>();
        process.getFlowElements().iterator()
            .forEachRemaining(flowElement -> processElementList.add(ProcessElement.builder().processId(process.getId())
                .version(processResponse.getVersion()).elementId(flowElement.getId()).elementName(flowElement.getName())
                .elementType(flowElement.getElementType().getTypeName()).build()));
        processMgtMapper.insertProcessElement(processElementList);

        // 保存 PROCESS_VERSION_HIS
        ProcessInfoHis processInfoHis =
            ProcessInfoHis.builder().processId(processInfo.getProcessId()).processName(processInfo.getProcessName())
                .formId(processInfo.getFormId()).bpmnXml(processInfo.getBpmnXml()).version(processResponse.getVersion())
                .deploymentId(String.valueOf(processResponse.getProcessDefinitionKey())).createdBy(userId).build();
        processMgtMapper.insertProcessInfoHis(processInfoHis);

        // 更新 PROCESS_INFO_T
        ProcessInfo updateProcessInfo = ProcessInfo.builder().processId(processInfo.getProcessId())
            .version(processResponse.getVersion()).isReleased(EngineConstants.RELEASE_ONE).updatedBy(userId).build();
        processMgtMapper.updateProcessInfo(updateProcessInfo);

        // 更新 PROCESS_NODE_ITEM_T
        if (processInfo.getIsReleased().intValue() == EngineConstants.RELEASE_ZERO.intValue()) {
            ProcessNodeItem processNodeItem = ProcessNodeItem.builder().processId(processInfo.getProcessId())
                .version(processResponse.getVersion()).build();
            processNodeItemMapper.updateProcessNodeItem(processNodeItem);
        }
        if (processInfo.getIsReleased().intValue() == EngineConstants.RELEASE_ONE.intValue()
            && !CollectionUtils.isEmpty(processNodeItemList)) {
            processNodeItemList.forEach(processNodeItem -> {
                processNodeItem.setIsReleased(EngineConstants.RELEASE_ONE);
                processNodeItem.setVersion(processResponse.getVersion());
            });
            processNodeItemMapper.batchInsertProcessNodeItem(processNodeItemList);
        }

        return BaseResponse.success();
    }

    @Override
    public List<ProcessInfoDto> queryProcessList(ProcessInfo processInfo) {
        return processMgtMapper.queryProcessList(processInfo);
    }

    /**
     * 分页查询流程监控列表信息
     *
     * @param processInfo
     *            流程查询对象
     * @return List
     */
    @Override
    public List<ProcessForMonitorDto> queryProcessForMonitorList(ProcessInfo processInfo) {
        return processMgtMapper.queryProcessForMonitorList(processInfo);
    }

    /**
     * 统计流程监控列表信息
     *
     * @param processInfo
     *            流程查询对象
     * @return ProcessForTotalDto
     */
    @Override
    public ProcessForTotalDto queryProcessTotalList(ProcessInfo processInfo) {
        List<ZeebeProcessInstance> zeebeProcessInstanceList = processMgtMapper.queryProcessTotalList(processInfo);
        ProcessForTotalDto processForTotalDto = new ProcessForTotalDto();
        processForTotalDto.setInitiateTotalNum(zeebeProcessInstanceList.size());
        processForTotalDto.setActivateInstanceNum(zeebeProcessInstanceList.stream()
            .filter(x -> x.getState().equalsIgnoreCase(EngineConstants.INSTANCE_STATE_ACTIVE)).count());
        processForTotalDto.setCompleteInstanceNum(zeebeProcessInstanceList.stream()
            .filter(x -> x.getState().equalsIgnoreCase(EngineConstants.INSTANCE_STATE_COMPLETED)).count());
        processForTotalDto.setTerminationInstanceNum(zeebeProcessInstanceList.stream()
            .filter(x -> x.getState().equalsIgnoreCase(EngineConstants.INSTANCE_STATE_TERMINATED)).count());
        return processForTotalDto;
    }

    /**
     * 统计当前流程下所有实例信息
     *
     * @param zeebeProcessInstanceQuery
     *            流程查询对象
     * @return ProcessForTotalDto
     */
    @Override
    public List<ZeebeProcessInstanceDto> queryInstanceList(ZeebeProcessInstanceQuery zeebeProcessInstanceQuery) {
        return processMgtMapper.queryInstanceList(zeebeProcessInstanceQuery);
    }

    /**
     * 统计当前流程下所有实例信息
     *
     * @param instanceId
     *            实例id
     * @return ProcessInstanceViewDto
     */
    @Override
    public List<ProcessInstanceViewDto> queryInstanceDetail(Long instanceId) {
        List<ProcessInstanceViewDto> processInstanceViewDtoList = processMgtMapper.queryInstanceDetail(instanceId);
        List<ProcessInstanceViewDto> newList = Lists.newArrayList();
        processInstanceViewDtoList.stream().collect(Collectors.groupingBy(ProcessInstanceViewDto::getKey))
            .forEach((k, y) -> {
                // 查询出最新的每个任务节点最新的信息
                ProcessInstanceViewDto viewDto =
                    y.stream().max(Comparator.comparing(ProcessInstanceViewDto::getPosition))
                        .orElse(ProcessInstanceViewDto.builder().build());
                // 筛选出最新的节点状态
                Integer delayStatus = viewDto.getIntent().equalsIgnoreCase(EngineConstants.ELEMENT_ACTIVATED) ? 0
                    : viewDto.getIntent().equalsIgnoreCase(EngineConstants.ELEMENT_COMPLETED) ? 1 : 2;
                // 筛选出最新的处理结果
                Integer delayResult = viewDto.getVariableValue().equalsIgnoreCase(ItemTypeOperation.AGREE.name()) ? 0
                    : Arrays.asList(ItemTypeOperation.FALLBACK.name(), ItemTypeOperation.FALLBACK_NODE.name())
                        .contains(viewDto.getVariableValue().toUpperCase(Locale.ROOT)) ? 1 : 2;
                // 构建出最新的节点状态对象
                ProcessInstanceViewDto processInstanceViewDto =
                    ProcessInstanceViewDto.builder().delayStatus(delayStatus).delayResult(delayResult).build();
                processInstanceViewDto.setElementId(viewDto.getElementId());
                processInstanceViewDto.setElementName(viewDto.getElementName());
                processInstanceViewDto.setElementStartTime(viewDto.getElementStartTime());
                processInstanceViewDto.setElementEndTime(viewDto.getElementEndTime());
                processInstanceViewDto.setElementType(viewDto.getElementType());
                newList.add(processInstanceViewDto);
            });
        return newList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse deleteProcess(List<String> list, String userId) {
        List<ProcessInfo> processInfoList = processMgtMapper.findProcessInfoByIdList(list);
        // 已发布的不能删除
        List<String> processNameList = Lists.newArrayList();
        List<String> stringList = Lists.newArrayList();
        processInfoList.forEach(x -> {
            // 已发布不删除
            if (x.getIsReleased() == 1) {
                processNameList.add(x.getProcessName());
            } else {
                stringList.add(x.getProcessId());
            }
        });
        if (!CollectionUtils.isEmpty(processNameList)) {
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setCode(CodeEnum.PROCESS_INFO_ALREADY_PUBLISH.getCode());
            baseResponse.setDesc(
                String.format(CodeEnum.PROCESS_INFO_ALREADY_PUBLISH.getDesc(), StringUtils.join(processNameList, ",")));
            return baseResponse;
        } else {
            if (!CollectionUtils.isEmpty(stringList)) {
                processMgtMapper.deleteProcess(stringList, userId);
            }
            return BaseResponse.success();
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse cancelPublishProcess(String processId, String userId) {
        processMgtMapper.cancelPublishProcess(processId, userId);
        ProcessInfo processInfo = processMgtMapper.findProcessInfoById(processId);
        processNodeItemMapper.updateCancelProcessNodeItem(
            ProcessNodeItem.builder().processId(processId).version(processInfo.getVersion()).build());
        return BaseResponse.success();
    }

    /**
     * 视图查询接口
     *
     * @param formName
     *            表单名字
     * @return map
     */
    @Override
    @DS("db1")
    public List<FormDto> queryFormList(String formName) {
        return processMgtMapper.queryFormList(formName);
    }

    /**
     * 视图查询接口
     *
     * @param formId
     *            表单id
     * @return map
     */
    @Override
    @DS("db1")
    public List<FormColumnDto> queryFormColumnList(String formId) {
        return processMgtMapper.queryFormColumnList(formId);
    }

    @Override
    @DS("db1")
    public List<UserInfo> queryUserList(String userName) {
        return processMgtMapper.findUserList(userName);
    }

    @Override
    @DS("db1")
    public List<RoleInfo> queryRoleList(String roleName) {
        return processMgtMapper.findRoleList(roleName);
    }
}
