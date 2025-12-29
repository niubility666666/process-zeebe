package com.rt.engine.task;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.rt.engine.common.config.RtConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableScheduling
public class DynamicCronTask implements SchedulingConfigurer {

    @Resource
    private RtConfig rtConfig;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(() -> {
            log.info("DynamicCronTask: syncInfo begin");
            log.info("DynamicCronTask: syncInfo end");
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(rtConfig.syncApaasServiceCron);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
