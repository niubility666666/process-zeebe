package com.rt.engine.mapper;

import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

import com.rt.engine.JunitBase;
import com.rt.engine.bean.entity.OperationLog;
import com.rt.engine.common.constants.OperationType;

/**
 * @author wuwanli
 * @date 2021/9/8
 */
public class OperationLogMapperTest extends JunitBase {

    @Resource
    private OperationLogMapper operationLogMapper;

    @Test
    public void testInsertOperationLogEntity() {
        OperationLog operationLog = OperationLog.builder().recordType(OperationType.DEPLOY.name()).recordDesc("流程部署")
            .operator("zhangSan").build();
        operationLogMapper.insertOperationLogEntity(operationLog);
    }

    @Test
    public void testQueryOperationLogList() {
        List<OperationLog> operationLogList = operationLogMapper.queryOperationLogList();
        System.out.println(operationLogList);
    }
}
