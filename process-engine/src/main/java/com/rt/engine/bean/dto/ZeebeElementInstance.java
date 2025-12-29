package com.rt.engine.bean.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class ZeebeElementInstance implements Serializable {

    private String id;

    private String bpmnElementType;

    private String elementId;

    private Long flowScopeKey;

    private String intent;

    private Long key;

    private Integer partitionId;

    private Long position;

    private Long processDefinitionKey;

    private Long processInstanceKey;

    private Long timestamp;

}
