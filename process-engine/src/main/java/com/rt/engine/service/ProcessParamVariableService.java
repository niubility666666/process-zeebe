package com.rt.engine.service;

import com.rt.engine.bean.entity.ProcessParamVariable;

import java.util.List;

public interface ProcessParamVariableService {

    /**
     * 查询参数
     *
     * @param processId
     *            参数变量
     * @return List
     */
    public List<ProcessParamVariable> selectProcessParamVariableListByProcessId(String processId);
}
