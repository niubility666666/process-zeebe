package com.rt.engine.task;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SyncApaasServiceTask {
    private final static String SYNC_APAAS_SERVICE_LOCK = "syncApaasServiceLock";

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private SyncApaasService syncApaasService;

    public void syncApaasService() {
        RLock rLock = redissonClient.getLock(SYNC_APAAS_SERVICE_LOCK);
        try {
            if (rLock.tryLock(0, TimeUnit.SECONDS)) {
                log.info("Redisson获取到分布式锁");
                log.info("syncApaasService begin ,currentDate is " + new Date());
                syncApaasService.syncApaasService();
            } else {
                log.info("Redisson没有获取到分布式锁");
                log.info("syncApaasService end ,currentDate is " + new Date());
            }
        } catch (Exception exception) {
            log.error("syncApaasService Exception ", exception);
        } finally {
            rLock.unlock();
        }
    }
}
