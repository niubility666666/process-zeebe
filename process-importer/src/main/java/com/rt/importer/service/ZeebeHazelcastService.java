package com.rt.importer.service;

import java.time.Duration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ZeebeHazelcastService {

    @Value("${rt.hazel-cast.connection}")
    private String hazelcastConnection;
    @Value("${rt.hazel-cast.connection-timeout}")
    private String hazelcastConnectionTimeout;

    @Resource
    private ZeebeImportService importService;

    private AutoCloseable closeable;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void start() {
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress(hazelcastConnection);

        final var connectionRetryConfig = clientConfig.getConnectionStrategyConfig().getConnectionRetryConfig();
        connectionRetryConfig.setClusterConnectTimeoutMillis(Duration.parse(hazelcastConnectionTimeout).toMillis());

        log.info("Connecting to Hazelcast '{}'", hazelcastConnection);

        final HazelcastInstance hazelcast = HazelcastClient.newHazelcastClient(clientConfig);

        log.info("Importing records from Hazelcast...");
        closeable = importService.importFrom(hazelcast);
    }

    @PreDestroy
    public void close() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }
}
