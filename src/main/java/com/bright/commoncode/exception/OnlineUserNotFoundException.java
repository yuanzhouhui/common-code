package com.bright.commoncode.exception;

/**
 * OnlineUserNotFoundException
 *
 * @author YuanZhouhui
 */
public class OnlineUserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    public OnlineUserNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public OnlineUserNotFoundException() {
        super("获取当前登录用户信息失败");
        this.message = "获取当前登录用户信息失败";
    }

    public OnlineUserNotFoundException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public OnlineUserNotFoundException(String message, Throwable e) {
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
