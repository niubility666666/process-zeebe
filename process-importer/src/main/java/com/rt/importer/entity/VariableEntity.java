package com.rt.importer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity(name = "ZEEBE_VARIABLE")
@Table(indexes = {@Index(name = "variable_processInstanceKeyIndex", columnList = "PROCESS_INSTANCE_KEY_"),})
public class VariableEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "POSITION_")
    private Long position;

    @Column(name = "PARTITION_ID_")
    private int partitionId;

    @Column(name = "NAME_")
    private String name;

    @Column(name = "VALUE_")
    @Lob
    private String value;

    @Column(name = "PROCESS_INSTANCE_KEY_")
    private long processInstanceKey;

    @Column(name = "SCOPE_KEY_")
    private long scopeKey;

    @Column(name = "STATE_")
    private String state;

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
