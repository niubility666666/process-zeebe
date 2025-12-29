package com.rt.importer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "ZEEBE_MESSAGE_SUBSCRIPTION")
public class MessageSubscriptionEntity {

    @Id
    @Column(name = "ID_")
    private String id;

    @Column(name = "MESSAGE_NAME_")
    private String messageName;

    @Column(name = "CORRELATION_KEY_")
    private String correlationKey;

    @Column(name = "PROCESS_INSTANCE_KEY_")
    private Long processInstanceKey;

    @Column(name = "ELEMENT_INSTANCE_KEY_")
    private Long elementInstanceKey;

    @Column(name = "PROCESS_DEFINITION_KEY_")
    private Long processDefinitionKey;

    @Column(name = "TARGET_FLOW_NODE_ID_")
    private String targetFlowNodeId;

    @Column(name = "STATE_")
    private String state;

    @Column(name = "TIMESTAMP_")
    private long timestamp;
}
