package com.rt.engine.common.util;

import com.rt.engine.common.constants.CodeEnum;
import com.rt.engine.common.exception.ProcessEngineException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeUtil {

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new ProcessEngineException(CodeEnum.UNKNOWN);
        }
    }
}
