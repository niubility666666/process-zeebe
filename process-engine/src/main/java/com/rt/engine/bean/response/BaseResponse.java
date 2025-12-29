package com.rt.engine.bean.response;

import com.rt.engine.common.constants.CodeEnum;

import lombok.Data;

@Data
public class BaseResponse {
    /**
     * 返回码
     */
    private String code;
    /**
     * 返回描述
     */
    private String desc;

    public static BaseResponse success() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(CodeEnum.SUCCESS.getCode());
        baseResponse.setDesc(CodeEnum.SUCCESS.getDesc());
        return baseResponse;
    }

    public static BaseResponse fail() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(CodeEnum.UNKNOWN.getCode());
        baseResponse.setDesc(CodeEnum.UNKNOWN.getDesc());
        return baseResponse;
    }

    public static BaseResponse fail(CodeEnum codeEnum) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(codeEnum.getCode());
        baseResponse.setDesc(codeEnum.getDesc());
        return baseResponse;
    }
}
