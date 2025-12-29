package com.rt.engine.bean.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicesQuery {

    /**
     * 服务id
     */
    private String serviceId;

    /**
     * 服务名称
     */
    private String serviceName;
}
