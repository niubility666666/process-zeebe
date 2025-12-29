package com.rt.engine.bean.response;

import com.rt.engine.common.constants.CodeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseVO extends BaseResponse {
    /**
     * 封装的返回对象
     */
    private Object data;

    public static ResponseVO success(Object data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(CodeEnum.SUCCESS.getCode());
        responseVO.setDesc(CodeEnum.SUCCESS.getDesc());
        responseVO.setData(data);
        return responseVO;
    }

    public static ResponseVO fail(CodeEnum codeEnum) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(codeEnum.getCode());
        responseVO.setDesc(codeEnum.getDesc());
        return responseVO;
    }
}
