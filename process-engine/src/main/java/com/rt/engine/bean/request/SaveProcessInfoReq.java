package com.rt.engine.bean.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.rt.engine.bean.dto.NodeItem;
import com.rt.engine.bean.dto.NodeItemForServiceTask;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveProcessInfoReq {
    /**
     * 表单ID
     */
    @NotBlank(message = "表单ID为空")
    private String formId;
    /**
     * bpmn
     */
    @NotBlank(message = "bpmn为空")
    private String bpmnXml;
    /**
     * 节点属性
     */
    private List<NodeItem> nodeItems;

    /**
     * serviceTask节点信息
     */
    private List<NodeItemForServiceTask> nodeItemForServiceTaskList;

    /**
     * 操作人
     */
    @JsonIgnore
    private String userId;
}
