package com.rt.importer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "ZEEBE_JOB")
public class JobEntity {
    @Id
    @Column(name = "KEY_")
    private long key;
    @Column(name = "PROCESS_INSTANCE_KEY_")
    private long processInstanceKey;
    @Column(name = "ELEMENT_INSTANCE_KEY_")
    private long elementInstanceKey;
    @Column(name = "JOB_TYPE_")
    private String jobType;
    @Column(name = "WORKER_")
    private String worker;
    @Column(name = "STATE_")
    private String state;
    @Column(name = "RETRIES_")
    private int retries;
    @Column(name = "TIMESTAMP_")
    private long timestamp;
}
