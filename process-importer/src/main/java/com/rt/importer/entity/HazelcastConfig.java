package com.rt.importer.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "ZEEBE_HAZELCAST_CONFIG")
public final class HazelcastConfig {

    @Id
    private String id;
    private long sequence;
}
