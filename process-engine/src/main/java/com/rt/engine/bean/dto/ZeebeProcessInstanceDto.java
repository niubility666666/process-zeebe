package com.rt.engine.bean.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ZeebeProcessInstanceDto {

    private String processId;

    private Long instanceId;

    /**
     * 状态 0 激活 1 终止 2 完成
     */
    private Integer state;

    private String startTime;

    private String endTime;
}
