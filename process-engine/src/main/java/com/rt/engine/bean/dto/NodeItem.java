package com.rt.engine.bean.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeItem {
    /**
     * 节点ID
     */
    @NotBlank(message = "节点ID为空")
    private String elementId;
    /**
     * 节点类型
     */
    @NotBlank(message = "节点类型为空")
    private String elementType;
    /**
     * 用户ID集合
     */
    private List<String> users;
    /**
     * 角色ID集合
     */
    private List<String> roles;
    /**
     * 操作权限集合
     */
    private List<String> operations;
    /**
     * 退回目标节点ID
     */
    private String fallbackId;
    /**
     * 表单字段列表
     */
    private String expression;
    /**
     * serviceTask的type
     */
    private String jobType;
}
