package com.rt.engine.controller;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

import com.rt.engine.JunitBase;
import com.rt.engine.bean.response.BaseResponse;

/**
 * @author yukun
 * @date 2021/11/4
 */

public class ProcessApiControllerTest extends JunitBase {

    @Resource
    private ProcessApiController processApiController;

    /**
     * 接口服务-流程列表分页查询
     */
    @Test
    public void openList() {
        // QueryProcessReq queryProcessReq = QueryProcessReq.builder().processId("").processName("").build();
        // PageResponseVO pageResponseVO = processApiController.queryProcessList(1, 10, queryProcessReq);
        // System.out.println(pageResponseVO.toString());
    }

    /**
     * 接口服务-查询实例状态
     */
    @Test
    public void queryInstanceStatus() {
        BaseResponse pageResponseVO = processApiController.queryInstanceStatus(2251799813857886L);
        System.out.println(pageResponseVO.toString());
    }

}
