package com.rt.engine.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessNodeItem {
    /** 属性ID */
    private Long itemId;
    /** 流程ID */
    private String processId;
    /** 版本 */
    private Integer version;
    /** 是否发布;0：未发布 1：已发布 */
    private Integer isReleased;
    /** 是否删除 */
    private Integer isDeleted;
    /** 节点ID */
    private String elementId;
    /** 节点类型 */
    private String elementType;
    /** 属性名称 */
    private String itemName;
    /** 属性类型;枚举 */
    private String itemType;
    /** 属性值 */
    private String itemValue;
    /** 扩展字段1 */
    private String ext1;
    /** 扩展字段2 */
    private String ext2;
    /** 扩展字段3 */
    private String ext3;
}
