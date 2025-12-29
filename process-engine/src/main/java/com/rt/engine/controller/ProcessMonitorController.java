package com.rt.engine.controller;

import com.rt.engine.bean.dto.*;
import com.rt.engine.bean.dto.*;
import com.rt.engine.bean.entity.ProcessInfo;
import com.rt.engine.bean.query.ZeebeProcessInstanceQuery;
import com.rt.engine.bean.response.PageResponseVO;
import com.rt.engine.bean.response.ResponseVO;
import com.rt.engine.service.ProcessApiService;
import com.rt.engine.service.ProcessMgtService;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("monitor")
public class ProcessMonitorController {

    @Autowired
    private ProcessMgtService processMgtService;

    @Autowired
    private ProcessApiService processApiService;

    @ApiOperation("查询流程监控列表")
    @GetMapping("/list/{limit}/{page}")
    public PageResponseVO queryProcessList(@PathVariable int limit, @PathVariable int page,
        @RequestParam(required = false) String processId, @RequestParam(required = false) String processName,
        @RequestParam(required = false) Integer isReleased) {
        PageHelper.startPage(page, limit);
        List<ProcessForMonitorDto> processForMonitorDtoList = processMgtService.queryProcessForMonitorList(
            ProcessInfo.builder().processId(StringUtils.isNotBlank(processId) ? processId.trim() : null)
                .processName(StringUtils.isNotBlank(processName) ? processName.trim() : null).isReleased(isReleased)
                .build());
        return PageResponseVO.success(processForMonitorDtoList);
    }

    @ApiOperation("查询流程监控列表统计")
    @GetMapping("/total")
    public ResponseVO queryProcessTotalList(@RequestParam(required = false) String processId,
        @RequestParam(required = false) String processName, @RequestParam(required = false) Integer isReleased) {
        ProcessForTotalDto processForTotalDto = processMgtService.queryProcessTotalList(
            ProcessInfo.builder().processId(StringUtils.isNotBlank(processId) ? processId.trim() : null)
                .processName(StringUtils.isNotBlank(processName) ? processName.trim() : null).isReleased(isReleased)
                .build());
        return ResponseVO.success(processForTotalDto);
    }

    @ApiOperation("查询流程监控列表")
    @GetMapping("/instance/list/{processId}/{limit}/{page}")
    public PageResponseVO queryInstanceList(@PathVariable int limit, @PathVariable int page,
        @PathVariable String processId, @RequestParam(required = false) Long instanceId,
        @RequestParam(required = false) Integer state) {
        PageHelper.startPage(page, limit);
        List<ZeebeProcessInstanceDto> processForMonitorDtoList = processMgtService.queryInstanceList(
            ZeebeProcessInstanceQuery.builder().processId(StringUtils.isNotBlank(processId) ? processId.trim() : null)
                .instanceId(instanceId).state(state).build());
        return PageResponseVO.success(processForMonitorDtoList);
    }

    @ApiOperation("查询流程实例详细列表")
    @GetMapping("/instance/detail/{instanceId}")
    public ResponseVO queryInstanceDetail(@PathVariable Long instanceId) {
        List<ProcessInstanceViewDto> processInstanceViewDtoList = processMgtService.queryInstanceDetail(instanceId);
        Map<String, Object> map = new ConcurrentHashMap<>(2);
        map.put("processInstanceViewDtoList", processInstanceViewDtoList);
        ProcessInstanceStatusDto processInstanceStatusDto = processApiService.queryInstanceStatus(instanceId);
        map.put("processInstanceStatusDto", processInstanceStatusDto);
        return ResponseVO.success(map);
    }
}
