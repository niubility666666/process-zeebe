package com.rt.engine.controller;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rt.engine.bean.response.BaseResponse;
import com.rt.engine.bean.response.ResponseVO;
import com.rt.engine.service.SyncApaasService;

import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Resource
    private SyncApaasService syncApaasService;

    @GetMapping("service/sync")
    public BaseResponse syncService() {
        syncApaasService.syncApaasService();
        return BaseResponse.success();
    }

    @GetMapping("bpmn")
    public ResponseVO getBpmnXml(@RequestParam("file") MultipartFile file) throws IOException {
        BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(file.getResource().getInputStream());
        return ResponseVO.success(Bpmn.convertToString(bpmnModelInstance));
    }

    // TODO a.b.c.d 使用这种格式来表示json层级关系

    // TODO 1. 搞清楚apaas v3版本 服务注册模型

    // TODO 2. 设计表模型

    // TODO 3. 服务同步、到我们自己库里面。只做增量同步
}
