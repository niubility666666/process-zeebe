package com.rt.engine.bean.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private String elementId;

    private String elementName;

    private String toTreatType;

    private List<String> toTreat;

    private TaskActionDto action;
}
