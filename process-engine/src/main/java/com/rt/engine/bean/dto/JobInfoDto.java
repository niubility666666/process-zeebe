package com.rt.engine.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobInfoDto {
    private String bpmnProcessId;
    private Long processInstanceKey;
    private Long elementInstanceKey;
    private String elementId;
    private String elementName;
    private String elementType;
    private Long jobKey;
    private String jobState;
    private String startTime;
    private Integer version;
}
