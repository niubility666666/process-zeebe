package com.rt.engine.common.exception;

import com.rt.engine.common.constants.CodeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProcessEngineException extends RuntimeException {
    /**
     * 错误码
     */
    protected String errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public ProcessEngineException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public ProcessEngineException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ProcessEngineException(CodeEnum codeEnum) {
        super(codeEnum.getDesc());
        this.errorCode = codeEnum.getCode();
        this.errorMsg = codeEnum.getDesc();
    }

    public ProcessEngineException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

    public ProcessEngineException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
