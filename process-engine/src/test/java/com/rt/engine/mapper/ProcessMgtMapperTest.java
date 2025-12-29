package com.rt.engine.mapper;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSONObject;
import com.rt.engine.JunitBase;
import com.rt.engine.bean.entity.ProcessInfo;

/**
 * @author wuwanli
 * @date 2021/10/29
 */
public class ProcessMgtMapperTest extends JunitBase {

    @Resource
    private ProcessMgtMapper processMgtMapper;

    @Test
    public void testInsertProcessInfo() {
        ProcessInfo processInfo = new ProcessInfo();
        processInfo.setProcessId("test");
        processMgtMapper.insertProcessInfo(processInfo);
        System.out.println(processInfo.getProcessId());
    }

    @Test
    public void testFindProcessInfoById() {
        ProcessInfo processInfo = processMgtMapper.findProcessInfoById("");
        System.out.println(JSONObject.toJSONString(processInfo));
    }
}
