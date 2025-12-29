package com.rt.engine.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeItemForServiceTask {

    /**
     * 参数id
     */
    private String paramId;

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 流程id
     */
    private String processId;

    /**
     * 节点id
     */
    private String elementId;

    /**
     * 节点name
     */
    private String elementName;

    /**
     * 是否是节点(0流程 1节点)
     */
    private Integer isNode;

    /**
     * 是否是节点(0变量 1参数)
     */
    private Integer isParam;

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 是否必填(0否 1是)
     */
    private Integer isMust;

    /**
     * 值域(0全局变量 1局部变量 2 输入参数 3节点输出 4自定义)
     */
    private Integer valueRegion;

    /**
     * 值或者示例
     */
    private String valueOrExample;

    /**
     * 说明
     */
    private String instructions;
}
