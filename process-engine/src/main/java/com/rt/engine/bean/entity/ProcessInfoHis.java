package com.rt.engine.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInfoHis {
    /** 流程ID */
    private String processId;
    /** 流程名称 */
    private String processName;
    /** 表单ID */
    private String formId;
    /** 流程定义内容 */
    private String bpmnXml;
    /** 版本 */
    private Integer version;
    /** 部署ID */
    private String deploymentId;
    /** 创建人 */
    private String createdBy;
    /** 创建时间 */
    private String createdTime;
}
