package com.rt.importer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Data;

@Data
@Entity(name = "ZEEBE_MESSAGE")
public class MessageEntity {

    @Id
    @Column(name = "KEY_")
    private long key;

    @Column(name = "NAME_")
    private String name;

    @Column(name = "CORRELATION_KEY_")
    private String correlationKey;

    @Column(name = "MESSAGE_ID_")
    private String messageId;

    @Column(name = "PAYLOAD_")
    @Lob
    private String payload;

    @Column(name = "STATE_")
    private String state;

    @Column(name = "TIMESTAMP_")
    private long timestamp;
}
