package com.rt.engine.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessElement {
    /** 流程ID */
    private String processId;
    /** 流程版本 */
    private Integer version;
    /** 节点ID */
    private String elementId;
    /** 节点名称 */
    private String elementName;
    /** 节点类型 */
    private String elementType;
}
