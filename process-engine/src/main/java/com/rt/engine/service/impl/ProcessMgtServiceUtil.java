package com.rt.engine.service.impl;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.model.xml.impl.util.ModelUtil;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.rt.engine.bean.dto.NodeItem;
import com.rt.engine.bean.entity.ProcessNodeItem;
import com.rt.engine.common.constants.CodeEnum;
import com.rt.engine.common.constants.ElementType;
import com.rt.engine.common.constants.EngineConstants;
import com.rt.engine.common.constants.ItemTypeEnum;
import com.rt.engine.common.constants.ItemTypeOperation;
import com.rt.engine.common.constants.SymbolConstants;
import com.rt.engine.common.exception.ProcessEngineException;
import com.google.common.collect.Lists;

import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import io.camunda.zeebe.model.bpmn.builder.ExclusiveGatewayBuilder;
import io.camunda.zeebe.model.bpmn.builder.ParallelGatewayBuilder;
import io.camunda.zeebe.model.bpmn.instance.ConditionExpression;
import io.camunda.zeebe.model.bpmn.instance.Gateway;
import io.camunda.zeebe.model.bpmn.instance.Process;
import io.camunda.zeebe.model.bpmn.instance.SequenceFlow;
import io.camunda.zeebe.model.bpmn.instance.UserTask;
import io.camunda.zeebe.model.bpmn.instance.bpmndi.BpmnDiagram;
import io.camunda.zeebe.model.bpmn.instance.bpmndi.BpmnEdge;
import io.camunda.zeebe.model.bpmn.instance.bpmndi.BpmnPlane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessMgtServiceUtil {
    /**
     * 字符转bpmn对象
     *
     * @param bpmnXml
     *            bpmn字符串
     * @return Process对象
     */
    public static BpmnModelInstance getBpmnModelInstanceFromXml(String bpmnXml) {
        try {
            BpmnModelInstance bpmnModelInstance =
                Bpmn.readModelFromStream(new ByteArrayInputStream(bpmnXml.getBytes(StandardCharsets.UTF_8)));
            return bpmnModelInstance;
        } catch (Exception ex) {
            log.error("getBpmnModelInstanceFromXml Exception:", ex);
            throw new ProcessEngineException(CodeEnum.BPMN_FORMAT_EXCEPTION);
        }
    }

    public static List<ProcessNodeItem> getProcessNodeItem(BpmnModelInstance bpmnModelInstance, String processId,
        List<NodeItem> nodeItems) {
        List<ProcessNodeItem> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(nodeItems)) {
            nodeItems.forEach(nodeItem -> {
                ProcessNodeItem processNodeItem =
                    ProcessNodeItem.builder().processId(processId).elementId(nodeItem.getElementId())
                        .elementType(nodeItem.getElementType()).isReleased(EngineConstants.RELEASE_ZERO).build();
                // 用户任务
                if (ElementType.USER_TASK.equals(nodeItem.getElementType())) {
                    if (!CollectionUtils.isEmpty(nodeItem.getUsers())) {
                        ProcessNodeItem userItem = new ProcessNodeItem();
                        BeanUtils.copyProperties(processNodeItem, userItem);
                        userItem.setItemType(ItemTypeEnum.ITEM_TYPE_USER.getId());
                        userItem.setItemName(ItemTypeEnum.ITEM_TYPE_USER.getName());
                        userItem.setItemValue(StringUtils.join(nodeItem.getUsers(), SymbolConstants.COMMA_EN));
                        list.add(userItem);
                    }
                    if (!CollectionUtils.isEmpty(nodeItem.getRoles())) {
                        ProcessNodeItem roleItem = new ProcessNodeItem();
                        BeanUtils.copyProperties(processNodeItem, roleItem);
                        roleItem.setItemType(ItemTypeEnum.ITEM_TYPE_ROLE.getId());
                        roleItem.setItemName(ItemTypeEnum.ITEM_TYPE_ROLE.getName());
                        roleItem.setItemValue(StringUtils.join(nodeItem.getRoles(), SymbolConstants.COMMA_EN));
                        list.add(roleItem);
                    }
                    if (!CollectionUtils.isEmpty(nodeItem.getOperations())) {
                        ProcessNodeItem operationItem = new ProcessNodeItem();
                        BeanUtils.copyProperties(processNodeItem, operationItem);
                        operationItem.setItemType(ItemTypeEnum.ITEM_TYPE_OPERATION.getId());
                        operationItem.setItemName(ItemTypeEnum.ITEM_TYPE_OPERATION.getName());
                        operationItem
                            .setItemValue(StringUtils.join(nodeItem.getOperations(), SymbolConstants.COMMA_EN));
                        // 退回上一步
                        if (nodeItem.getOperations().contains(ItemTypeOperation.FALLBACK.getId())) {
                            ModelElementInstance modelElementInstance =
                                bpmnModelInstance.getModelElementById(nodeItem.getElementId());
                            if (modelElementInstance instanceof UserTask) {
                                UserTask userTask = (UserTask)modelElementInstance;
                                List<String> fallbackIds = Lists.newArrayList();
                                userTask.getIncoming()
                                    .forEach(flow -> fallbackIds.addAll(getFallbackIds(flow.getSource())));
                                operationItem.setExt1(StringUtils.join(fallbackIds, SymbolConstants.COMMA_EN));
                            }
                        }
                        // 退回指定节点
                        if (nodeItem.getOperations().contains(ItemTypeOperation.FALLBACK_NODE.getId())) {
                            operationItem.setExt1(nodeItem.getFallbackId());
                        }
                        list.add(operationItem);
                    }
                }
                if (ElementType.SEQUENCE_FLOW.equals(nodeItem.getElementType())) {
                    if (StringUtils.isNotBlank(nodeItem.getExpression())) {
                        ProcessNodeItem expressionItem = new ProcessNodeItem();
                        BeanUtils.copyProperties(processNodeItem, expressionItem);
                        expressionItem.setItemType(ItemTypeEnum.ITEM_TYPE_EXPRESSION.getId());
                        expressionItem.setItemName(ItemTypeEnum.ITEM_TYPE_EXPRESSION.getName());
                        expressionItem.setItemValue(nodeItem.getExpression());
                        list.add(expressionItem);
                    }
                }
            });
        }
        return list;
    }

    private static List<String> getFallbackIds(ModelElementInstance modelElementInstance) {
        List<String> list = Lists.newArrayList();
        if (modelElementInstance instanceof UserTask) {
            UserTask parentUserTask = (UserTask)modelElementInstance;
            list.add(parentUserTask.getId());
        }
        if (modelElementInstance instanceof Gateway) {
            Gateway gateway = (Gateway)modelElementInstance;
            gateway.getIncoming().forEach(flow -> list.addAll(getFallbackIds(flow.getSource())));
        }
        return list;
    }

    public static List<NodeItem> getNodeItem(List<ProcessNodeItem> processNodeItemList) {
        List<NodeItem> nodeItemList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(processNodeItemList)) {
            Set<String> elementIdSet =
                processNodeItemList.stream().map(ProcessNodeItem::getElementId).collect(Collectors.toSet());
            elementIdSet.forEach(elementId -> {
                NodeItem nodeItem = new NodeItem();
                nodeItem.setElementId(elementId);
                for (ProcessNodeItem processNodeItem : processNodeItemList) {
                    if (elementId.equals(processNodeItem.getElementId())) {
                        nodeItem.setElementType(processNodeItem.getElementType());
                        if (StringUtils.isNotBlank(processNodeItem.getItemValue())) {
                            if (ElementType.USER_TASK.equals(processNodeItem.getElementType())) {
                                if (ItemTypeEnum.ITEM_TYPE_USER.getId().equals(processNodeItem.getItemType())) {
                                    nodeItem.setUsers(
                                        Arrays.asList(processNodeItem.getItemValue().split(SymbolConstants.COMMA_EN)));
                                }
                                if (ItemTypeEnum.ITEM_TYPE_ROLE.getId().equals(processNodeItem.getItemType())) {
                                    nodeItem.setRoles(
                                        Arrays.asList(processNodeItem.getItemValue().split(SymbolConstants.COMMA_EN)));
                                }
                                if (ItemTypeEnum.ITEM_TYPE_OPERATION.getId().equals(processNodeItem.getItemType())) {
                                    nodeItem.setOperations(
                                        Arrays.asList(processNodeItem.getItemValue().split(SymbolConstants.COMMA_EN)));
                                }
                                nodeItem.setFallbackId(processNodeItem.getExt1());
                            }
                            if (ElementType.SEQUENCE_FLOW.equals(processNodeItem.getElementType())) {
                                if (ItemTypeEnum.ITEM_TYPE_EXPRESSION.getId().equals(processNodeItem.getItemType())) {
                                    nodeItem.setExpression(processNodeItem.getItemValue());
                                }
                            }
                        }
                    }
                }
                nodeItemList.add(nodeItem);
            });
        }
        return nodeItemList;
    }

    public static void addGateWay(BpmnModelInstance bpmnModelInstance, String elementId, String fallbackIds) {
        // 根据用户任务id 找到任务节点
        ModelElementInstance modelElementInstance = bpmnModelInstance.getModelElementById(elementId);
        if (modelElementInstance instanceof UserTask) {
            UserTask userTask = (UserTask)modelElementInstance;
            SequenceFlow outSequenceFlow = userTask.getOutgoing().stream().findFirst().orElseThrow();
            ModelElementType elementType = outSequenceFlow.getElementType();

            // 删除连线坐标
            BpmnDiagram bpmnDiagram =
                bpmnModelInstance.getModelElementsByType(BpmnDiagram.class).stream().findFirst().orElseThrow();
            BpmnPlane bpmnPlane = bpmnDiagram.getBpmnPlane();
            BpmnEdge bpmnEdge = outSequenceFlow.getDiagramElement();
            bpmnPlane.removeChildElement(bpmnEdge);

            // 删除连线
            Collection<Process> processes = bpmnModelInstance.getModelElementsByType(Process.class);
            Process process = processes.stream().findFirst().orElseThrow();
            process.removeChildElement(outSequenceFlow);

            // 增加一个排他网关
            ExclusiveGatewayBuilder exclusiveGatewayBuilder = userTask.builder().exclusiveGateway();

            // 审批通过
            exclusiveGatewayBuilder.sequenceFlowId(ModelUtil.getUniqueIdentifier(elementType))
                .conditionExpression("approve=\"agree\"").connectTo(outSequenceFlow.getTarget().getId());
            String[] fallbackIdArray = fallbackIds.split(SymbolConstants.COMMA_EN);
            if (fallbackIdArray.length == 1) {
                exclusiveGatewayBuilder.sequenceFlowId(ModelUtil.getUniqueIdentifier(elementType))
                    .conditionExpression("approve=\"fallback\"").connectTo(fallbackIdArray[0]);
            }
            if (fallbackIdArray.length > 1) {
                ParallelGatewayBuilder parallelGatewayBuilder =
                    exclusiveGatewayBuilder.sequenceFlowId(ModelUtil.getUniqueIdentifier(elementType))
                        .conditionExpression("approve=\"fallback\"").parallelGateway();
                Arrays.asList(fallbackIdArray).forEach(fallbackId -> {
                    parallelGatewayBuilder.sequenceFlowId(ModelUtil.getUniqueIdentifier(elementType))
                        .connectTo(fallbackId);
                });
            }
        }
    }

    /**
     * 转换表达式中的${} 字符 样例1：=a=1 and b=2 样例2：=a="a" or b="b"
     *
     * @param bpmnModelInstance
     *            流程实体
     */
    public static void convertConditionExpression(BpmnModelInstance bpmnModelInstance) {
        Collection<Gateway> gateways = bpmnModelInstance.getModelElementsByType(Gateway.class);
        gateways.forEach(gateway -> gateway.getOutgoing().forEach(sequenceFlow -> {
            ConditionExpression conditionExpression = sequenceFlow.getConditionExpression();
            if (conditionExpression != null) {
                String textContent = conditionExpression.getTextContent();
                if (StringUtils.isNotBlank(textContent)) {
                    textContent = textContent.replace(SymbolConstants.DOLLAR, SymbolConstants.BLANK)
                        .replace(SymbolConstants.LEFT_BRACKET, SymbolConstants.BLANK)
                        .replace(SymbolConstants.RIGHT_BRACKET, SymbolConstants.BLANK);
                }
                if (!textContent.startsWith(SymbolConstants.EQUAL)) {
                    textContent = SymbolConstants.EQUAL + textContent;
                }
                conditionExpression.setTextContent(textContent);
            }
        }));
    }
}
