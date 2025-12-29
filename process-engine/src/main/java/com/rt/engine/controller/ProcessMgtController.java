package com.rt.engine.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.rt.engine.common.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rt.engine.bean.dto.ProcessInfoDto;
import com.rt.engine.bean.entity.ProcessInfo;
import com.rt.engine.bean.request.SaveProcessInfoReq;
import com.rt.engine.bean.response.BaseResponse;
import com.rt.engine.bean.response.PageResponseVO;
import com.rt.engine.bean.response.ResponseVO;
import com.rt.engine.common.annotation.OperationLog;
import com.rt.engine.common.constants.EngineConstants;
import com.rt.engine.common.constants.OperationType;
import com.rt.engine.common.constants.SymbolConstants;
import com.rt.engine.service.ProcessMgtService;
import com.github.pagehelper.PageHelper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;

@RestController
@ApiModel("流程管理")
@RequestMapping("/mgt")
public class ProcessMgtController {

    @Resource
    private ProcessMgtService processMgtService;

    @Resource
    private RedisUtil redisUtil;

    @ApiOperation("流程列表查询接口")
    @GetMapping("/list/{limit}/{page}")
    public PageResponseVO queryProcessList(@PathVariable int limit, @PathVariable int page,
        @RequestParam(required = false) String processId, @RequestParam(required = false) String processName,
        @RequestParam(required = false) Integer isReleased) {
        PageHelper.startPage(page, limit);
        List<ProcessInfoDto> processInfoList = processMgtService.queryProcessList(
            ProcessInfo.builder().processId(StringUtils.isNotBlank(processId) ? processId.trim() : null)
                .processName(StringUtils.isNotBlank(processName) ? processName.trim() : null).isReleased(isReleased)
                .build());
        processInfoList.forEach(x -> {
            if (StringUtils.isNotBlank(x.getVersion())) {
                x.setVersion(SymbolConstants.VERSION_UPPER + x.getVersion());
            } else {
                x.setVersion("-");
            }
        });
        return PageResponseVO.success(processInfoList);
    }

    @ApiOperation("保存userTask流程接口")
    @PostMapping("/save")
    @OperationLog(type = OperationType.ADD, desc = "保存流程")
    public BaseResponse saveProcessInfo(HttpServletRequest httpServletRequest,
        @RequestBody @Valid SaveProcessInfoReq saveProcessInfoReq) {
        saveProcessInfoReq.setUserId((String)httpServletRequest.getAttribute(EngineConstants.USER_ID));
        return processMgtService.saveProcessInfo(saveProcessInfoReq);
    }

    @ApiOperation("保存serviceTask流程接口")
    @PostMapping("/saveForServiceTask")
    @OperationLog(type = OperationType.ADD, desc = "保存流程")
    public BaseResponse saveForServiceTask(HttpServletRequest httpServletRequest,
        @RequestBody @Valid SaveProcessInfoReq saveProcessInfoReq) {
        saveProcessInfoReq.setUserId((String)httpServletRequest.getAttribute(EngineConstants.USER_ID));
        return processMgtService.saveProcessInfo(saveProcessInfoReq);
    }

    @ApiOperation("修改流程接口")
    @PutMapping("/modify")
    @OperationLog(type = OperationType.MODIFY, desc = "修改流程")
    public BaseResponse modifyProcessInfo(HttpServletRequest httpServletRequest,
        @RequestBody @Valid SaveProcessInfoReq saveProcessInfoReq) {
        saveProcessInfoReq.setUserId((String)httpServletRequest.getAttribute(EngineConstants.USER_ID));
        return processMgtService.modifyProcessInfo(saveProcessInfoReq);
    }

    @ApiOperation("修改serviceTask流程接口")
    @PutMapping("/modifyForServiceTask")
    @OperationLog(type = OperationType.MODIFY, desc = "修改流程")
    public BaseResponse modifyForServiceTask(HttpServletRequest httpServletRequest,
        @RequestBody @Valid SaveProcessInfoReq saveProcessInfoReq) {
        saveProcessInfoReq.setUserId((String)httpServletRequest.getAttribute(EngineConstants.USER_ID));
        return processMgtService.modifyProcessInfo(saveProcessInfoReq);
    }

    @ApiOperation("查询流程详情")
    @GetMapping
    public ResponseVO queryProcessDetail(@RequestParam String processId) {
        return processMgtService.queryProcessDetail(processId.trim());
    }

    @ApiOperation("发布流程接口")
    @PostMapping("/publish/{processId}")
    @OperationLog(type = OperationType.DEPLOY, desc = "发布流程")
    public BaseResponse publishProcess(HttpServletRequest httpServletRequest, @PathVariable String processId) {
        return processMgtService.publishProcess(processId,
            (String)httpServletRequest.getAttribute(EngineConstants.USER_ID));
    }

    @ApiOperation("删除流程接口")
    @DeleteMapping("/{processIds}")
    @OperationLog(type = OperationType.DELETE, desc = "删除流程")
    public BaseResponse deleteProcess(HttpServletRequest httpServletRequest, @PathVariable String processIds) {
        return processMgtService.deleteProcess(Arrays.asList(processIds.split(SymbolConstants.COMMA_EN)),
            (String)httpServletRequest.getAttribute(EngineConstants.USER_ID));
    }

    @ApiOperation("取消发布流程接口")
    @PostMapping("/publish/cancel/{processId}")
    @OperationLog(type = OperationType.CANCEL, desc = "取消发布流程")
    public BaseResponse cancelPublishProcess(HttpServletRequest httpServletRequest, @PathVariable String processId) {
        return processMgtService.cancelPublishProcess(processId,
            (String)httpServletRequest.getAttribute(EngineConstants.USER_ID));
    }

    @ApiOperation("查询表单列表")
    @GetMapping({"/form/list", "/form/list/{formName}"})
    public ResponseVO queryFormList(@PathVariable(required = false) String formName) {
        return ResponseVO.success(processMgtService.queryFormList(formName));
    }

    @ApiOperation("查询表单字段列表")
    @GetMapping("/formColumn/list/{formId}")
    public ResponseVO queryFormColumnList(@PathVariable String formId) {
        return ResponseVO.success(processMgtService.queryFormColumnList(formId));
    }

    @ApiOperation("查询用户列表")
    @GetMapping({"/user/list", "/user/list/{userName}"})
    public ResponseVO queryUserList(@PathVariable(required = false) String userName) {
        return ResponseVO.success(processMgtService.queryUserList(userName));
    }

    @ApiOperation("查询角色列表")
    @GetMapping({"/role/list", "/role/list/{roleName}"})
    public ResponseVO queryRoleList(@PathVariable(required = false) String roleName) {
        return ResponseVO.success(processMgtService.queryRoleList(roleName));
    }

    @ApiOperation("生成processId编号")
    @GetMapping("getProcessId")
    public ResponseVO getProcessId() {
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Object obj = redisUtil.get(dateStr);
        String processId = null;
        if (obj != null) {
            Integer number = Integer.parseInt(String.valueOf(obj)) + 1;
            StringBuilder stringBuilder = new StringBuilder(dateStr);
            stringBuilder.append("0".repeat(Math.max(0, 4 - number.toString().length())));
            processId = stringBuilder.append(number).toString();
            redisUtil.set(dateStr, number.toString(), 24 * 60 * 60, TimeUnit.SECONDS);
        } else {
            processId = dateStr + "0001";
            redisUtil.set(dateStr, "1", 24 * 60 * 60, TimeUnit.SECONDS);
        }
        return ResponseVO.success(processId);
    }
}
