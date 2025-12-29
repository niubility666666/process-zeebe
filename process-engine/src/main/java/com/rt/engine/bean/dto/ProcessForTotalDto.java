package com.rt.engine.bean.dto;

import lombok.Data;

@Data
public class ProcessForTotalDto {

    /**
     * 发起总数
     */
    private Integer initiateTotalNum;

    /**
     * 激活实例数
     */
    private Long activateInstanceNum;

    /**
     * 完成实例数
     */
    private Long completeInstanceNum;

    /**
     * 终止实例数
     */
    private Long terminationInstanceNum;

}
