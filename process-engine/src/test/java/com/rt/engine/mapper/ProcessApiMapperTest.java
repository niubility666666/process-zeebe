package com.rt.engine.mapper;

import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

import com.rt.engine.JunitBase;
import com.rt.engine.bean.dto.JobInfoDto;

/**
 * @author wuwanli
 * @date 2021/11/8
 */
public class ProcessApiMapperTest extends JunitBase {

    @Resource
    private ProcessApiMapper processApiMapper;

    @Test
    public void testFindNextJobsByInstanceKey() {
        long processInstanceKey = 2251799813843685l;
        List<JobInfoDto> jobInfoDtoList = processApiMapper.findNextJobsByInstanceKey(processInstanceKey);
        jobInfoDtoList.forEach(jobInfoDto -> System.out.println(jobInfoDto.toString()));
    }
}
