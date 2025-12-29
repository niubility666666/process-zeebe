package com.rt.engine.bean.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ZeebeProcessInstance implements Serializable {

    private Long key;

    private String bpmnProcessId;

    private Long end;

    private Long parentElementInstanceKey;

    private Long parentProcessInstanceKey;

    private Integer partitionId;

    private Long processDefinitionKey;

    private Long start;

    private String state;

    private Integer version;

}
