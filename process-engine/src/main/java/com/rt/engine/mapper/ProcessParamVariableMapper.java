package com.rt.engine.mapper;

import java.util.List;

import com.rt.engine.bean.entity.ProcessParamVariable;

public interface ProcessParamVariableMapper {

    /**
     * 新增参数变量
     * 
     * @param processParamVariable
     *            参数变量
     */
    public void insertProcessParamVariable(ProcessParamVariable processParamVariable);

    /**
     * 删除参数变量
     *
     * @param processParamVariable
     *            参数变量
     */
    public void deleteProcessParamVariable(ProcessParamVariable processParamVariable);

    /**
     * 查询参数
     *
     * @param processParamVariable
     *            参数变量
     * @return List
     */
    public List<ProcessParamVariable> selectProcessParamVariableList(ProcessParamVariable processParamVariable);

    /**
     * 查询参数
     *
     * @param processId
     *            参数变量
     * @return List
     */
    public List<ProcessParamVariable> selectProcessParamVariableListByProcessId(String processId);

    /**
     * 批量插入属性信息
     * 
     * @param list
     *            集合
     */
    void batchInsertProcessParamVariable(List<ProcessParamVariable> list);

    /**
     * 删除参数信息
     * 
     * @param processId
     *            流程id
     */
    void deleteProcessParamVariableByProcessId(String processId);
}
