package com.rt.engine.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInfo {
    /** 流程ID */
    private String processId;
    /** 流程名称 */
    private String processName;
    /** 表单ID */
    private String formId;
    /** 流程定义内容 */
    private String bpmnXml;
    /** 是否发布;0：未发布 1：已发布 */
    private Integer isReleased;
    /** 版本 */
    private Integer version;
    /** 是否删除;0：未删除 1：已删除 */
    private Integer isDeleted;
    /** 创建人 */
    private String createdBy;
    /** 创建时间 */
    private String createdTime;
    /** 更新人 */
    private String updatedBy;
    /** 更新时间 */
    private String updatedTime;
}
