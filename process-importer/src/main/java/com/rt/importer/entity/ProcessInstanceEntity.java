package com.rt.importer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "ZEEBE_PROCESS_INSTANCE")
public class ProcessInstanceEntity {
    @Id
    @Column(name = "KEY_")
    private long key;

    @Column(name = "PARTITION_ID_")
    private int partitionId;

    @Column(name = "PROCESS_DEFINITION_KEY_")
    private long processDefinitionKey;

    @Column(name = "BPMN_PROCESS_ID_")
    private String bpmnProcessId;

    @Column(name = "VERSION_")
    private int version;

    @Column(name = "STATE_")
    private String state;

    @Column(name = "START_")
    private long start;

    @Column(name = "END_")
    private Long end;

    @Column(name = "PARENT_PROCESS_INSTANCE_KEY_")
    private Long parentProcessInstanceKey;

    @Column(name = "PARENT_ELEMENT_INSTANCE_KEY_")
    private Long parentElementInstanceKey;
}
