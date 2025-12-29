package com.rt.engine.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "beagle")
public class PropertiesConfig {
    private final Apaas apaas;
    private final Cron cron;

    public PropertiesConfig() {
        this.apaas = new Apaas();
        this.cron = new Cron();
    }

    @Data
    public static class Apaas {
        /**
         * Apaas 服务代理地址
         */
        private String proxyUrl;
    }

    @Data
    public static class Cron {
        /**
         * Apaas 服务代理地址
         */
        private String apaasService;
    }

}
