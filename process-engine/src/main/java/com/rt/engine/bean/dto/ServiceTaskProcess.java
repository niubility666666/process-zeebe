package com.rt.engine.bean.dto;

import java.util.List;

import com.rt.engine.bean.entity.ProcessInfo;
import com.rt.engine.bean.entity.ServiceTaskVariable;

import lombok.Data;

@Data
public class ServiceTaskProcess {
    /**
     * 流程信息
     */
    private ProcessInfo processInfo;
    /**
     * 全局变量
     */
    private List<ServiceTaskVariable> globalVariableList;
    /**
     * 输入配置
     */
    private List<ServiceTaskVariable> inputVariableList;
    /**
     * 输出配置
     */
    private List<ServiceTaskVariable> outputVariableList;
    /**
     * 节点属性列表
     */
    private List<ServiceTaskNode> serviceTaskNodeList;
}
