package com.rt.importer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Data;

@Data
@Entity(name = "ZEEBE_ERROR")
public class ErrorEntity {

    @Id
    @Column(name = "POSITION_")
    private long position;

    @Column(name = "ERROR_EVENT_POSITION_")
    private long errorEventPosition;

    @Column(name = "PROCESS_INSTANCE_KEY_")
    private long processInstanceKey;

    @Column(name = "EXCEPTION_MESSAGE_")
    @Lob
    private String exceptionMessage;

    @Column(name = "STACKTRACE_")
    @Lob
    private String stacktrace;

    @Column(name = "TIMESTAMP_")
    private long timestamp;
}
