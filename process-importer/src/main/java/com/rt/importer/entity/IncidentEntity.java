package com.rt.importer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Data;

@Data
@Entity(name = "ZEEBE_INCIDENT")
public class IncidentEntity {

    @Id
    @Column(name = "KEY_")
    private long key;

    @Column(name = "BPMN_PROCESS_ID_")
    private String bpmnProcessId;

    @Column(name = "PROCESS_DEFINITION_KEY_")
    private long processDefinitionKey;

    @Column(name = "PROCESS_INSTANCE_KEY_")
    private long processInstanceKey;

    @Column(name = "ELEMENT_INSTANCE_KEY_")
    private long elementInstanceKey;

    @Column(name = "JOB_KEY_")
    private long jobKey;

    @Column(name = "ERROR_TYPE_")
    private String errorType;

    @Column(name = "ERROR_MSG_")
    @Lob
    private String errorMessage;

    @Column(name = "CREATED_")
    private long created;

    @Column(name = "RESOLVED_")
    private Long resolved;
}
