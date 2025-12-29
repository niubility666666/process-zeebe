package com.rt.engine.bean.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryProcessReq {

    /**
     * 模板id
     */
    private String processId;

    /**
     * 模板名称
     */
    private String processName;


    private Integer isReleased;
}
