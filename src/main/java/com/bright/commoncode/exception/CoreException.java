package com.bright.commoncode.exception;

import com.bright.commoncode.enums.ErrorCodeEnum;

/**
 * @author yuanzhouhui
 * @date 2023-02-23 14:37
 */
public class CoreException extends RuntimeException {
    private ErrorCodeEnum codeEnum;

    public CoreException(ErrorCodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.codeEnum = codeEnum;
    }

    public CoreException(ErrorCodeEnum codeEnum, Throwable cause) {
        super(codeEnum.getMessage(), cause);
        this.codeEnum = codeEnum;
    }

    public CoreException(ErrorCodeEnum codeEnum, String msg) {
        super(msg);
        this.codeEnum = codeEnum;
    }

    public ErrorCodeEnum getCodeEnum() {
        return this.codeEnum;
    }
}
