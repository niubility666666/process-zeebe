package com.rt.importer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * application自定义key 配置
 * 
 * @author wuwanli
 * @version 1.0
 * @date 2021/8/4
 */
@Data
@ConfigurationProperties(prefix = PropertyConfig.PREFIX)
public class PropertyConfig {
    public static final String PREFIX = "beagle";
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
