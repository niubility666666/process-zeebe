package com.rt.engine.bean.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MouldDto {

    private String processId;

    private String processName;

    private String version;

    private String updateTime;

    private String bpmnXml;

    private List<String> parameter;
}
