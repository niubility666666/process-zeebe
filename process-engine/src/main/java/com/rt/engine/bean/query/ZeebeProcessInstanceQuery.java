package com.rt.engine.bean.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ZeebeProcessInstanceQuery {


    private String processId;

    private Long instanceId;

    /**
     * 状态 0 激活 1 完成 2 终止
     */
    private Integer state;

    private String startTimeStart;

    private String startTimeEnd;

    private String endTimeStart;

    private String endTimeEnd;
}
