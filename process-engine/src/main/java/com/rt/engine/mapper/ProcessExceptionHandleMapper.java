package com.rt.engine.mapper;

import java.util.List;

import com.rt.engine.bean.entity.ProcessExceptionHandle;

public interface ProcessExceptionHandleMapper {

    /**
     * 新增异常信息
     * 
     * @param processExceptionHandle
     *            异常
     */
    public void insertProcessExceptionHandle(ProcessExceptionHandle processExceptionHandle);

    /**
     * 删除异常信息
     * 
     * @param processExceptionHandle
     *            异常
     */
    public void deleteProcessExceptionHandle(ProcessExceptionHandle processExceptionHandle);

    /**
     * 查询异常列表
     * 
     * @param processExceptionHandle
     *            异常
     * @return List
     */
    public List<ProcessExceptionHandle> selectProcessExceptionHandleList(ProcessExceptionHandle processExceptionHandle);
}
