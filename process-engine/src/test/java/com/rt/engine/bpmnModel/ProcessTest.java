package com.rt.engine.bpmnModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.model.xml.impl.util.ModelUtil;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.junit.jupiter.api.Test;

import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import io.camunda.zeebe.model.bpmn.builder.ExclusiveGatewayBuilder;
import io.camunda.zeebe.model.bpmn.instance.Process;
import io.camunda.zeebe.model.bpmn.instance.SequenceFlow;
import io.camunda.zeebe.model.bpmn.instance.ServiceTask;
import io.camunda.zeebe.model.bpmn.instance.UserTask;
import io.camunda.zeebe.model.bpmn.instance.bpmndi.BpmnDiagram;
import io.camunda.zeebe.model.bpmn.instance.bpmndi.BpmnEdge;
import io.camunda.zeebe.model.bpmn.instance.bpmndi.BpmnPlane;

/**
 * @author wuwanli
 * @date 2021/10/22
 */
public class ProcessTest {

    @Test
    public void addJobType() throws FileNotFoundException {
        File file = new File("C:\\Users\\wuwanli\\Downloads\\serviceTask.bpmn");
        final BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(new FileInputStream(file));
        String taskAId = "Activity_0uvsdjg";
        String taskBId = "Activity_0lnde2j";
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put(taskAId, "foo");
        typeMap.put(taskBId, "bar");

        typeMap.keySet().stream().forEach(id -> {
            if (bpmnModelInstance.getModelElementById(id) instanceof ServiceTask) {
                ServiceTask task = bpmnModelInstance.getModelElementById(id);
                task.builder().zeebeJobType(typeMap.get(id)).done();
            }
        });
        System.out.println(Bpmn.convertToString(bpmnModelInstance));
    }

    @Test
    public void addGateWay(BpmnModelInstance bpmnModelInstance, String userTaskId, int reject)
        throws FileNotFoundException {
        File file = new File("C:\\Users\\wuwanli\\Downloads\\test.bpmn");
        bpmnModelInstance = Bpmn.readModelFromStream(new FileInputStream(file));
        userTaskId = "Activity_1gu6fnm";
        reject = 0;
        // 根据用户任务id 找到任务节点
        UserTask userTask = bpmnModelInstance.getModelElementById(userTaskId);
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
            .conditionExpression("approve='Y'").connectTo(outSequenceFlow.getTarget().getId());

        if (1 == reject) {
            // 退回到上一步处理人
            userTask.getIncoming().stream().iterator().forEachRemaining(
                sequenceFlow -> exclusiveGatewayBuilder.sequenceFlowId(ModelUtil.getUniqueIdentifier(elementType))
                    .conditionExpression("approve='N'").connectTo(sequenceFlow.getSource().getId()));
        } else {
            // 退回到发起人
            Collection<UserTask> userTasks = bpmnModelInstance.getModelElementsByType(UserTask.class);
            UserTask initiatorTask = userTasks.stream().findFirst().orElseThrow();
            exclusiveGatewayBuilder.sequenceFlowId(ModelUtil.getUniqueIdentifier(elementType))
                .conditionExpression("approve='N'").connectTo(initiatorTask.getId());
        }

        System.out.println(Bpmn.convertToString(bpmnModelInstance));
    }
}
