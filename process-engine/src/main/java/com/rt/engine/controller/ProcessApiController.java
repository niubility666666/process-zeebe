package com.rt.engine.controller;

import java.util.List;

import javax.annotation.Resource;

import com.rt.engine.bean.response.BaseResponse;
import com.rt.engine.common.constants.SymbolConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.rt.engine.bean.dto.MouldDto;
import com.rt.engine.bean.entity.ProcessInfo;
import com.rt.engine.bean.response.PageResponseVO;
import com.rt.engine.bean.response.ResponseVO;
import com.rt.engine.common.annotation.OperationLog;
import com.rt.engine.common.constants.EngineConstants;
import com.rt.engine.common.constants.OperationType;
import com.rt.engine.service.ProcessApiService;
import com.github.pagehelper.PageHelper;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ProcessApiController {

    @Resource
    private ProcessApiService processApiService;

    @ApiOperation("创建实例")
    @OperationLog(type = OperationType.CREATE, desc = "创建实例")
    @PostMapping("/instance/create/{bpmnProcessId}")
    public ResponseVO createInstance(@PathVariable String bpmnProcessId,
        @RequestBody(required = false) JSONObject jsonObject) {
        return ResponseVO.success(processApiService.createInstance(bpmnProcessId, jsonObject));
    }

    @ApiOperation("取消流程实例")
    @OperationLog(type = OperationType.CANCEL, desc = "取消流程实例")
    @PostMapping("/instance/cancel/{processInstanceKey}")
    public BaseResponse cancelInstance(@PathVariable long processInstanceKey) {
        return processApiService.cancelInstance(processInstanceKey);
    }

    @ApiOperation("完成节点任务")
    @OperationLog(type = OperationType.COMPLETE, desc = "完成节点任务")
    @PostMapping("/instance/complete/{processInstanceKey}/{elementId}/{action}")
    public ResponseVO completeTask(@PathVariable long processInstanceKey, @PathVariable String elementId,
        @PathVariable String action) {
        return ResponseVO.success(processApiService.completeTask(processInstanceKey, elementId, action));
    }

    @ApiOperation("查询流程模板列表")
    @GetMapping("/process/list/{limit}/{page}")
    public PageResponseVO queryProcessList(@PathVariable int limit, @PathVariable int page,
        @RequestParam(required = false) String processId, @RequestParam(required = false) String processName) {
        PageHelper.startPage(page, limit);
        List<MouldDto> mouldDtoList = processApiService.queryProcessList(
            ProcessInfo.builder().processId(StringUtils.isNotBlank(processId) ? processId.trim() : null)
                .processName(StringUtils.isNotBlank(processName) ? processName.trim() : null)
                .isReleased(EngineConstants.RELEASE_ONE).build());
        mouldDtoList.forEach(x -> {
            if (StringUtils.isNotBlank(x.getVersion())) {
                x.setVersion(SymbolConstants.VERSION_UPPER + x.getVersion());
            }
        });
        return PageResponseVO.success(mouldDtoList);
    }

    @ApiOperation("查询实例状态")
    @GetMapping("/instance/status/{processInstanceKey}")
    public ResponseVO queryInstanceStatus(@PathVariable long processInstanceKey) {
        return ResponseVO.success(processApiService.queryInstanceStatus(processInstanceKey));
    }
}
