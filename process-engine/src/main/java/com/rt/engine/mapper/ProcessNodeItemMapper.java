package com.rt.engine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.rt.engine.bean.entity.ProcessNodeItem;

@Repository
public interface ProcessNodeItemMapper {
    /**
     * 批量查询流程节点属性
     *
     * @param processNodeItemList
     *            流程节点信息
     */
    void batchInsertProcessNodeItem(@Param("list") List<ProcessNodeItem> processNodeItemList);

    /**
     * 删除未发布的流程节点属性
     *
     * @param processId
     *            流程ID
     */
    void deleteProcessNodeItemById(String processId);

    /**
     * 流程发布更新节点的版本信息和发布状态
     *
     * @param processNodeItem
     *            流程节点属性
     */
    void updateProcessNodeItem(ProcessNodeItem processNodeItem);

    /**
     * 流程发布更新节点的版本信息和发布状态
     *
     * @param processNodeItem
     *            流程节点属性
     */
    void updateCancelProcessNodeItem(ProcessNodeItem processNodeItem);

    /**
     * 查询流程节点属性
     *
     * @param processNodeItem
     *            请求参数
     * @return 节点属性集合
     */
    List<ProcessNodeItem> findProcessNodeItemList(ProcessNodeItem processNodeItem);

    /**
     * 查询当前版本流程下的nodeItem信息
     * 
     * @param processId
     *            流程id
     * @param version
     *            版本号
     * @return List
     */
    List<ProcessNodeItem> findAlreadyProcessNodeItemList(@Param("processId") String processId,
        @Param("version") Integer version);

    /**
     * 获取当前实例对应流程最新版本的ProcessNodeItem
     * 
     * @param processInstanceKey
     *            流程id
     * @return List
     */
    List<ProcessNodeItem> findProcessNodeItemListByInstanceKey(long processInstanceKey);
}
