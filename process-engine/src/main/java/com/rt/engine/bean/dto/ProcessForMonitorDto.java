package com.rt.engine.bean.dto;

import lombok.Data;

@Data
public class ProcessForMonitorDto {

    /** 流程ID */
    private String processId;

    /** 流程名称 */
    private String processName;

    /**
     * 平均耗时分钟
     */
    private Integer avgTimeConsuming;

    /**
     * 激活实例数
     */
    private Integer activateInstanceNum;

    /**
     * 终止实例数
     */
    private Integer terminationInstanceNum;

    /**
     * 完成实例数
     */
    private Integer completeInstanceNum;
}
