package com.rt.engine.bean.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInstanceViewDto implements Serializable {

    @JsonIgnore
    private Long key;

    /**
     * 节点id
     */
    private String elementId;

    /**
     * 节点名称
     */
    private String elementName;

    /**
     * 开始时间
     */
    private String elementStartTime;

    /**
     * 结束时间
     */
    private String elementEndTime;

    /**
     * 处理状态
     */
    @JsonIgnore
    private String intent;

    /**
     * 0 完成 1 激活 2终止
     */
    private Integer delayStatus;

    /**
     * 0 通过 1退回 2 拒绝
     */
    private Integer delayResult;

    /**
     *
     */
    @JsonIgnore
    private Long position;

    /**
     * 处理结果
     */
    @JsonIgnore
    private String variableValue;

    /**
     * 节点类型
     */
    private String elementType;
}
