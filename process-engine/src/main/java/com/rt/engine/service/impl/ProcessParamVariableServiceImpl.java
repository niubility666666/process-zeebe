package com.rt.engine.service.impl;

import com.rt.engine.bean.entity.ProcessParamVariable;
import com.rt.engine.mapper.ProcessParamVariableMapper;
import com.rt.engine.service.ProcessParamVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessParamVariableServiceImpl implements ProcessParamVariableService {

    @Autowired
    private ProcessParamVariableMapper processParamVariableMapper;

    /**
     * 查询参数
     *
     * @param processId 参数变量
     * @return List
     */
    @Override
    public List<ProcessParamVariable> selectProcessParamVariableListByProcessId(String processId) {
        return processParamVariableMapper.selectProcessParamVariableListByProcessId(processId);
    }
}
