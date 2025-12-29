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
public class TaskActionDto {

    private boolean agree;

    private boolean reject;

    private boolean fallback;

    private List<String> fallbackIds;
}
