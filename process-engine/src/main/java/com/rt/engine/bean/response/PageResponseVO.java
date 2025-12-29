package com.rt.engine.bean.response;

import java.util.List;

import com.rt.engine.common.constants.CodeEnum;
import com.github.pagehelper.PageInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageResponseVO extends ResponseVO {
    /**
     * 当前页数
     */
    private Integer page;
    /**
     * 每页条数
     */
    private Integer limit;
    /**
     * 总页数
     */
    private Long total;

    public static PageResponseVO success(List<?> list) {
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PageResponseVO responseVO = new PageResponseVO();
        responseVO.setCode(CodeEnum.SUCCESS.getCode());
        responseVO.setDesc(CodeEnum.SUCCESS.getDesc());
        responseVO.setData(pageInfo.getList());
        responseVO.setTotal(pageInfo.getTotal());
        responseVO.setLimit(pageInfo.getPageSize());
        responseVO.setPage(pageInfo.getPageNum());
        return responseVO;
    }
}
