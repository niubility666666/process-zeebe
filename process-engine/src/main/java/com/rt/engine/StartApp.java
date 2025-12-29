package com.rt.engine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.camunda.zeebe.spring.client.EnableZeebeClient;

@MapperScan("com.rt.engine.mapper")
@EnableTransactionManagement
@EnableZeebeClient
@ConfigurationPropertiesScan
@SpringBootApplication
public class StartApp {
    public static void main(String[] args) {
        SpringApplication.run(StartApp.class, args);
    }
}
