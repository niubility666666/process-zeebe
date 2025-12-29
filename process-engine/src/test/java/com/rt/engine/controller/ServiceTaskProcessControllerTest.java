package com.rt.engine.controller;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.rt.engine.JunitBase;
import com.rt.engine.bean.dto.ServiceTaskNode;
import com.rt.engine.bean.dto.ServiceTaskProcess;
import com.rt.engine.bean.entity.ProcessInfo;
import com.rt.engine.bean.entity.ServiceTaskVariable;

/**
 * @author wuwanli
 * @date 2022/3/9
 */
public class ServiceTaskProcessControllerTest extends JunitBase {

    public static void main(String[] args) {
        ServiceTaskProcess serviceTaskProcess = new ServiceTaskProcess();
        ProcessInfo processInfo = new ProcessInfo();
        processInfo.setProcessName("流程名称");
        processInfo.setBpmnXml("bpmnXml");
        serviceTaskProcess.setProcessInfo(processInfo);
        // 全局变量
        List<ServiceTaskVariable> globalServiceTaskVariableList = new ArrayList<>();
        ServiceTaskVariable globalServiceTaskVariable = new ServiceTaskVariable();
        globalServiceTaskVariable.setId("全局变量ID");
        globalServiceTaskVariable.setParamName("变量名");
        globalServiceTaskVariable.setParamType("参数类型");
        globalServiceTaskVariable.setParamValue("参数值");
        globalServiceTaskVariable.setParamDesc("参数说明");
        globalServiceTaskVariableList.add(globalServiceTaskVariable);
        serviceTaskProcess.setGlobalVariableList(globalServiceTaskVariableList);
        // 输入配置
        List<ServiceTaskVariable> inputServiceTaskVariableList = new ArrayList<>();
        ServiceTaskVariable inputServiceTaskVariable = new ServiceTaskVariable();
        inputServiceTaskVariable.setId("参数唯一ID");
        inputServiceTaskVariable.setParamName("参数名称");
        inputServiceTaskVariable.setParamType("参数类型");
        inputServiceTaskVariable.setRequired(false);
        inputServiceTaskVariable.setParamValue("参数值");
        inputServiceTaskVariable.setParamDesc("说明");
        List<ServiceTaskVariable> inputServiceTaskVariableChildrenList = new ArrayList<>();
        ServiceTaskVariable inputServiceTaskVariableChildren = new ServiceTaskVariable();
        inputServiceTaskVariableChildren.setId("参数唯一ID");
        inputServiceTaskVariableChildren.setParamName("参数名称");
        inputServiceTaskVariableChildren.setParamType("参数类型");
        inputServiceTaskVariableChildren.setRequired(false);
        inputServiceTaskVariableChildren.setParamValue("参数值");
        inputServiceTaskVariableChildren.setParamDesc("说明");
        inputServiceTaskVariableChildrenList.add(inputServiceTaskVariableChildren);
        inputServiceTaskVariable.setChildren(inputServiceTaskVariableChildrenList);
        inputServiceTaskVariableList.add(inputServiceTaskVariable);
        serviceTaskProcess.setInputVariableList(inputServiceTaskVariableList);
        // 任务节点
        List<ServiceTaskNode> serviceTaskNodeList = new ArrayList<>();
        ServiceTaskNode serviceTaskNode = new ServiceTaskNode();
        serviceTaskNode.setElementId("节点ID");
        serviceTaskNode.setElementName("节点名称");
        serviceTaskNode.setServiceId("服务ID");
        serviceTaskNode.setServiceName("服务名称");
        serviceTaskNode.setTimeout(10);
        serviceTaskNode.setTimeoutUnit("s:秒 m: 分钟");
        serviceTaskNode.setRetryNum(1);

        List<ServiceTaskVariable> nodeInputServiceTaskVariableList = new ArrayList<>();
        ServiceTaskVariable nodeInputServiceTaskVariable = new ServiceTaskVariable();
        nodeInputServiceTaskVariable.setId("参数唯一ID");
        nodeInputServiceTaskVariable.setParamName("参数名称");
        nodeInputServiceTaskVariable.setParamType("参数类型");
        nodeInputServiceTaskVariable.setRequired(false);
        nodeInputServiceTaskVariable.setParamValueType("值域:全局变量");
        nodeInputServiceTaskVariable.setParamValue("节点ID:参数ID.参数ID");
        nodeInputServiceTaskVariable.setParamDesc("说明");
        nodeInputServiceTaskVariableList.add(nodeInputServiceTaskVariable);
        serviceTaskNode.setInputVariableList(nodeInputServiceTaskVariableList);

        List<ServiceTaskVariable> nodeOutputServiceTaskVariableList = new ArrayList<>();
        ServiceTaskVariable nodeOutputServiceTaskVariable = new ServiceTaskVariable();
        nodeOutputServiceTaskVariable.setId("参数唯一ID");
        nodeOutputServiceTaskVariable.setParamName("参数名称");
        nodeOutputServiceTaskVariable.setParamType("参数类型");
        nodeOutputServiceTaskVariable.setParamValue("示例值");
        nodeOutputServiceTaskVariable.setParamDesc("说明");
        nodeOutputServiceTaskVariableList.add(nodeOutputServiceTaskVariable);
        serviceTaskNode.setOutputVariableList(nodeOutputServiceTaskVariableList);
        serviceTaskNodeList.add(serviceTaskNode);
        serviceTaskProcess.setServiceTaskNodeList(serviceTaskNodeList);

        // 输出
        List<ServiceTaskVariable> outputServiceTaskVariableList = new ArrayList<>();
        ServiceTaskVariable outputServiceTaskVariable = new ServiceTaskVariable();
        outputServiceTaskVariable.setId("参数唯一ID");
        outputServiceTaskVariable.setParamName("参数名称");
        nodeInputServiceTaskVariable.setParamType("参数类型");
        outputServiceTaskVariable.setRequired(false);
        outputServiceTaskVariable.setParamValueType("值域:全局变量");
        outputServiceTaskVariable.setParamValue("节点ID:参数ID.参数ID");
        outputServiceTaskVariable.setParamDesc("说明");
        outputServiceTaskVariableList.add(outputServiceTaskVariable);
        serviceTaskProcess.setOutputVariableList(outputServiceTaskVariableList);
        System.out.println(JSONObject.toJSONString(serviceTaskProcess));
    }
}
