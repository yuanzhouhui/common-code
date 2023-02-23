package com.bright.commoncode.exception;

/**
 * LoginException
 *
 * @author YuanZhouhui
 */
public class TokenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    public TokenException(String message) {
        super(message);
        this.message = message;
    }

    public TokenException() {
        super("TOKEN无效");
        this.message = "TOKEN无效";
    }

    public TokenException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public TokenException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
