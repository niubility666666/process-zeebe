package com.rt.engine.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class BeagleConfig {

    @Value("${beagle.apaas.proxy-url}")
    public String apaasProxyUrl;
    @Value("${beagle.cron.apaas-service}")
    public String syncApaasServiceCron;
}
