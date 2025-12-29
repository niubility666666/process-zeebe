package com.rt.engine.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rt.engine.bean.entity.ProcessParamVariable;
import com.rt.engine.service.ProcessParamVariableService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;

@EnableZeebeClient
@Component
public class ZeebeWorkerComponent {

    @Autowired
    private ProcessParamVariableService processParamVariableService;

    // This code does not limit the variables resolves
    // That means the worker always fetches all variables to support expressions/placeholders
    // as a workaround until https://github.com/zeebe-io/zeebe/issues/3417 is there
    // @ZeebeWorker(name = "serviceTaskWorker")
    public void handleFooJob(final JobClient client, final ActivatedJob job) {
        // 查出当前实例的参数设置
        List<ProcessParamVariable> processParamVariableList =
            processParamVariableService.selectProcessParamVariableListByProcessId(job.getBpmnProcessId());
        // 查出当前节点的参数设置
        List<ProcessParamVariable> variableList = processParamVariableList.stream()
            .filter(x -> x.getElementId().equals(job.getElementId())).collect(Collectors.toList());
        // 拼出一个属性节点树
        List<ProcessParamVariable> processParamVariableTree = getProcessParamVariableTree(
            variableList.stream().filter(x -> StringUtils.isBlank(x.getParentParamId())).collect(Collectors.toList()),
            variableList);
        // HttpUtil.send();
        // 流程扭转
        client.newCompleteCommand(job.getKey()).variables(job.getVariables()).send().join();
    }

    private List<ProcessParamVariable> getProcessParamVariableTree(List<ProcessParamVariable> parentVariableList,
        List<ProcessParamVariable> processParamVariableList) {
        return parentVariableList;
    }
}
