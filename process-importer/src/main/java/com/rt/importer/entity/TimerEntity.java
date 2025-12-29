package com.rt.importer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "ZEEBE_TIMER")
public class TimerEntity {

    @Id
    @Column(name = "KEY_")
    private long key;

    @Column(name = "PROCESS_DEFINITION_KEY_")
    private long processDefinitionKey;

    @Column(name = "PROCESS_INSTANCE_KEY_")
    private Long processInstanceKey;

    @Column(name = "ELEMENT_INSTANCE_KEY_")
    private Long elementInstanceKey;

    @Column(name = "TARGET_ELEMENT_ID_")
    private String targetElementId;

    @Column(name = "DUE_DATE_")
    private long dueDate;

    @Column(name = "REPETITIONS")
    private int repetitions;

    @Column(name = "STATE_")
    private String state;

    @Column(name = "TIMESTAMP_")
    private long timestamp;
}
