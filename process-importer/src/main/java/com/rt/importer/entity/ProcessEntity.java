package com.rt.importer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Data;

@Data
@Entity(name = "ZEEBE_PROCESS")
public class ProcessEntity {
    @Id
    @Column(name = "KEY_")
    private long key;

    @Column(name = "BPMN_PROCESS_ID_")
    private String bpmnProcessId;

    @Column(name = "VERSION_")
    private int version;

    @Lob
    @Column(name = "RESOURCE_")
    private String resource;

    @Column(name = "TIMESTAMP_")
    private long timestamp;

}
