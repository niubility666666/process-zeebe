package com.rt.engine.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rt.engine.bean.dto.ServiceTaskProcess;
import com.rt.engine.bean.response.BaseResponse;

@RestController
@RequestMapping("/serviceTask")
public class ServiceTaskProcessController {

    @PostMapping
    public BaseResponse saveServiceTaskProcess(@RequestBody ServiceTaskProcess serviceTaskProcess) {
        return BaseResponse.success();
    }
}
