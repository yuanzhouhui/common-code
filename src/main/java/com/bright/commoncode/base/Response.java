package com.bright.commoncode.base;

import com.bright.commoncode.enums.ErrorCodeEnum;
import com.bright.commoncode.enums.MsgTypeEnum;
import lombok.EqualsAndHashCode;

/**
 * @author yuanzhouhui
 * @date 2023-02-23 14:48
 */
@EqualsAndHashCode
public class Response<T> {
    private boolean success;
    private Integer statusCode;
    private T data;
    private String message;
    private MsgTypeEnum msgType;

    public Response() {
    }

    public Response(boolean success, Integer statusCode, T data, String message, MsgTypeEnum msgType) {
        this.success = success;
        this.statusCode = statusCode;
        this.data = data;
        this.message = message;
        this.msgType = msgType;
    }

    public static <T> Response<T> with() {
        return with(ErrorCodeEnum.SUCCESS, null);
    }

    public static <T> Response<T> with(T data) {
        return with(ErrorCodeEnum.SUCCESS, data);
    }

    public static Response<String> withMsg(String msg) {
        return with(ErrorCodeEnum.SUCCESS, msg, msg, null);
    }

    public static Response<String> withMsg(String msg, MsgTypeEnum msgType) {
        return with(ErrorCodeEnum.SUCCESS, msg, msg, msgType);
    }

    public static <T> Response<T> with(ErrorCodeEnum errorCode, T data) {
        return with(errorCode, data, errorCode.getMessage(), null);
    }

    public static <T> Response<T> with(ErrorCodeEnum errorCode, T data, MsgTypeEnum msgType) {
        return with(errorCode, data, errorCode.getMessage(), msgType);
    }

    public static <T> Response<T> with(ErrorCodeEnum errorCode, T data, String msg, MsgTypeEnum msgType) {
        boolean isSuccess = ErrorCodeEnum.SUCCESS.equals(errorCode);
        if (null == msgType) {
            msgType = isSuccess ? MsgTypeEnum.INFO : MsgTypeEnum.ERROR;
        }

        return (Response<T>) builder().data(data).statusCode(errorCode.getErrorCode()).success(isSuccess).msgType(msgType).message(msg).build();
    }

    public static <T> ResponseBuilder<T> builder() {
        return new ResponseBuilder<>();
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MsgTypeEnum getMsgType() {
        return this.msgType;
    }

    public void setMsgType(MsgTypeEnum msgType) {
        this.msgType = msgType;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Response;
    }

    public String toString() {
        return "Response(success=" + this.isSuccess() + ", statusCode=" + this.getStatusCode() + ", data=" + this.getData() + ", message=" + this.getMessage() + ", msgType=" + this.getMsgType() + ")";
    }

    public static class ResponseBuilder<T> {
        private boolean success;
        private Integer statusCode;
        private T data;
        private String message;
        private MsgTypeEnum msgType;

        ResponseBuilder() {
        }

        public ResponseBuilder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public ResponseBuilder<T> statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder<T> msgType(MsgTypeEnum msgType) {
            this.msgType = msgType;
            return this;
        }

        public Response<T> build() {
            return new Response(this.success, this.statusCode, this.data, this.message, this.msgType);
        }

        public String toString() {
            return "Response.ResponseBuilder(success=" + this.success + ", statusCode=" + this.statusCode + ", data=" + this.data + ", message=" + this.message + ", msgType=" + this.msgType + ")";
        }
    }
}
