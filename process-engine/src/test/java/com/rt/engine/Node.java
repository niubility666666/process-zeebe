package com.rt.engine;

import lombok.Data;

import java.util.List;

/**
 * @author yukun
 * @date 2021/9/15
 */

@Data
public class Node {

    private String id;
    private Integer width;
    private Integer height;

    private List<Integer> coordinate;

    private Meta meta;

    @Data
    public static class Meta{
        private String id;
        private String label;
        private String name;
        private String type;
        private String content;
        private List<String> plugins;
    }
}
