package com.rt.engine.bean.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProcessExceptionHandle implements Serializable {
    /**
     * 主键id
     */
    private String exceptionHandleId;

    /**
     * 流程id
     */
    private String processId;

    /**
     * 节点id
     */
    private String elementId;

    /**
     * 超时时长
     */
    private Integer timeoutNum;

    /**
     * 超时单位(0秒1分)
     */
    private String timeoutUnit;

    /**
     * 重试次数
     */
    private Integer retryNum;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private String createdTime;

    /**
     * 修改人
     */
    private String updatedBy;

    /**
     * 修改时间
     */
    private String updatedTime;
}
