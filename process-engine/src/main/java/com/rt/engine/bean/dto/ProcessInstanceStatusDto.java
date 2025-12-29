package com.rt.engine.bean.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessInstanceStatusDto {

    /**
     * 流程实例id
     */
    private long instanceId;

    /**
     * 实例对应的模板版本号
     */
    private String version;

    /**
     * 流程是否已完成
     */
    private boolean isOver;

    /**
     * 流程运行详细记录
     */
    private List<TaskDto> nodeTaskList;

    private List<TaskLog> taskLogList;
}
