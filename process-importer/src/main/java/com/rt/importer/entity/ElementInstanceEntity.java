package com.rt.importer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity(name = "ZEEBE_ELEMENT_INSTANCE")
@Table(indexes = {@Index(name = "element_instance_processInstanceKeyIndex", columnList = "PROCESS_INSTANCE_KEY_"),})
public class ElementInstanceEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "POSITION_")
    private Long position;

    @Column(name = "PARTITION_ID_")
    private int partitionId;

    @Column(name = "KEY_")
    private long key;

    @Column(name = "INTENT_")
    private String intent;

    @Column(name = "PROCESS_INSTANCE_KEY_")
    private long processInstanceKey;

    @Column(name = "ELEMENT_ID_")
    private String elementId;

    @Column(name = "BPMN_ELEMENT_TYPE_")
    private String bpmnElementType;

    @Column(name = "FLOW_SCOPE_KEY_")
    private long flowScopeKey;

    @Column(name = "PROCESS_DEFINITION_KEY_")
    private long processDefinitionKey;

    @Column(name = "TIMESTAMP_")
    private long timestamp;

    public final String getGeneratedIdentifier() {
        return this.partitionId + "-" + this.position;
    }

    @PrePersist
    private void prePersistDeriveIdField() {
        setId(getGeneratedIdentifier());
    }
}
