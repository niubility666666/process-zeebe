package com.rt.engine.bean.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class TaskLog {

    private String elementId;

    private String elementName;

    private String startTime;

    private String endTime;

    /**
     * 处理对象类型
     */
    private String toTreatType;

    /**
     * 处理对象
     */
    private List<String> toTreat;

    /**
     * 处理动作
     */
    private String doTaskResult;
}
