package com.rt.engine.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

import com.rt.engine.JunitBase;
import com.rt.engine.bean.dto.TaskDto;

/**
 * @author wuwanli
 * @date 2021/11/18
 */
public class ProcessApiServiceTest extends JunitBase {

    @Resource
    private ProcessApiService processApiService;

    @Test
    public void testQueryNextTaskList() {
        long processInstanceKey = Long.valueOf("2251799813970274");
        List<TaskDto> taskDtoList = processApiService.queryNextTaskList(processInstanceKey);
        taskDtoList.forEach(taskDto -> System.out.println(taskDto));
    }
}
