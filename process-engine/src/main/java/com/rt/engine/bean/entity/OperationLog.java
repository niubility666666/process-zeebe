package com.rt.engine.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationLog {
    /**
     * 记录id 序列生成
     */
    private Integer recordId;
    /**
     * 操作类型
     */
    private String recordType;
    /**
     * 操作描述
     */
    private String recordDesc;
    /**
     * 记录时间 系统获取
     */
    private String recordTime;
    /**
     * 操作人
     */
    private String operator;
}
