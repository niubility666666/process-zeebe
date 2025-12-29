package com.rt.engine;

import lombok.Data;

import java.util.List;

/**
 * @author yukun
 * @date 2021/9/15
 */

@Data
public class Link {

    private String id;

    private String startId;

    private String endId;

    private List<Integer> startAt;

    private List<Integer> endAt;
}
