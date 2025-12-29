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

import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("bpmn")
    public ResponseVO getBpmnXml(@RequestParam("file") MultipartFile file) throws IOException {
        BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(file.getResource().getInputStream());
        return ResponseVO.success(Bpmn.convertToString(bpmnModelInstance));
    }
}
