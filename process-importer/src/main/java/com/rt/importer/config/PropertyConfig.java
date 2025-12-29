package com.rt.importer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = PropertyConfig.PREFIX)
public class PropertyConfig {
    public static final String PREFIX = "rt";
    private final HazelCast hazelCast;

    public PropertyConfig() {
        this.hazelCast = new HazelCast();
    }

    @Data
    public static class HazelCast {
        private String connection;
        private String connectionTimeout;
    }
}
