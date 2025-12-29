package com.rt.engine.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessVariable {
    /** 任务ID */
    private Long jobKey;
    /** 流程ID */
    private String processId;
    /** 流程实例ID */
    private Long processInstanceKey;
    /** 节点ID */
    private String elementId;
    /** 节点实例ID */
    private Long elementInstanceKey;
    /** 节点名称 */
    private String elementName;
    /** 节点开始时间 */
    private String elementStartTime;
    /** 节点结束时间 */
    private String elementEndTime;
    /** 变量名称 */
    private String variableName;
    /** 变量值 */
    private String variableValue;
    /** 创建人 */
    private String createdBy;
    /** 创建时间 */
    private String createdTime;

}
