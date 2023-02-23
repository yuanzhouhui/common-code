package com.bright.commoncode.handler.handler;


import com.alibaba.excel.exception.ExcelRuntimeException;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.bright.commoncode.base.Response;
import com.bright.commoncode.enums.ErrorCodeEnum;
import com.bright.commoncode.enums.MsgTypeEnum;
import com.bright.commoncode.exception.CoreException;
import com.bright.commoncode.exception.OnlineUserNotFoundException;
import com.bright.commoncode.exception.TokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局异常处理
 *
 * @author yjj
 * @since 2022/8/1
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private static final String ERROR_MSG = "系统处理失败，请稍后重试";
    /**
     * 权限码异常
     */
    /*@ExceptionHandler(NotPermissionException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<String> handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        log.error("请求地址'{}',权限码校验失败'{}'", request.getRequestURI(), e.getMessage());
        return Response.with(ErrorCodeEnum.NOT_AUTH_OPTION, null, "没有访问权限，请联系管理员授权", MsgTypeEnum.ERROR);
    }*/

    /**
     * 角色权限异常
     */
    /*@ExceptionHandler(NotRoleException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<String> handleNotRoleException(NotRoleException e, HttpServletRequest request) {
        log.error("请求地址'{}',角色权限校验失败'{}'", request.getRequestURI(), e.getMessage());
        return Response.with(ErrorCodeEnum.NOT_AUTH_OPTION, null, "没有访问权限，请联系管理员授权", MsgTypeEnum.ERROR);
    }*/

    /**
     * 认证失败
     */
    /*@ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<String> handleNotLoginException(NotLoginException e, HttpServletRequest request) {
        log.error("请求地址'{}',认证失败'{}',无法访问系统资源", request.getRequestURI(), e.getMessage());
        return Response.with(ErrorCodeEnum.INVALID_TOKEN, null, "认证失败，无法访问系统资源", MsgTypeEnum.ERROR);
    }*/

    /**
     * 无效认证
     */
    /*@ExceptionHandler(SameTokenInvalidException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<String> handleSameTokenInvalidException(SameTokenInvalidException e, HttpServletRequest request) {
        log.error("请求地址'{}',内网认证失败'{}',无法访问系统资源", request.getRequestURI(), e.getMessage());
        return Response.with(ErrorCodeEnum.INVALID_TOKEN, null, "认证失败，无法访问系统资源", MsgTypeEnum.ERROR);
    }*/

    /**
     * 请求方式不支持
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<String> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                HttpServletRequest request) {
        log.error("请求地址'{}',不支持'{}'请求", request.getRequestURI(), e.getMethod(), e);
        return Response.with(ErrorCodeEnum.UNSUPPORT, null, "请求方式不支持", MsgTypeEnum.ERROR);
    }

    @ExceptionHandler(CoreException.class)
    @ResponseStatus(HttpStatus.OK)
    Response<String> handleBadRequestException(CoreException ex, HandlerMethod handlerMethod) {
        log.error("业务异常:", ex);
        return Response.with(ex.getCodeEnum(), null, ex.getMessage(), MsgTypeEnum.ERROR);
    }

    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.OK)
    Response<String> handleTokenException(TokenException ex) {
        log.warn("TokenException:{}", ex.getMessage());
        return Response.with(ErrorCodeEnum.INVALID_TOKEN, null, ex.getMessage(), MsgTypeEnum.ERROR);
    }

    @ExceptionHandler(OnlineUserNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    Response<String> handleTokenException(OnlineUserNotFoundException ex) {
        log.warn("OnlineUserNotFoundException:{}", ex.getMessage());
        return Response.with(ErrorCodeEnum.INVALID_TOKEN, null, ex.getMessage(), MsgTypeEnum.ERROR);
    }

    /**
     * excel导出异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ExcelRuntimeException.class)
    public Response<String> handleExcelRuntimeException(ExcelRuntimeException e) {
        log.error("excel导出异常", e);
        return Response.with(ErrorCodeEnum.FILE_READ_FAIL, null, "excel处理失败", MsgTypeEnum.ERROR);
    }

    /**
     * 主键或UNIQUE索引，数据重复异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DuplicateKeyException.class)
    public Response<String> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        log.error("请求地址'{}',数据库中已存在记录'{}'", requestUri, e.getMessage());
        return Response.with(ErrorCodeEnum.ALREADY_EXISTS, null, "数据库中已存在该记录，请联系管理员确认", MsgTypeEnum.ERROR);

    }

    /**
     * Mybatis系统异常 通用处理
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MyBatisSystemException.class)
    public Response<String> handleCannotFindDataSourceException(MyBatisSystemException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String message = e.getMessage();
        if (message.contains("CannotFindDataSourceException")) {
            log.error("请求地址'{}', 未找到数据源", requestUri);
            return Response.with(ErrorCodeEnum.DB_CONNECTION_FAIL, null, "未找到数据源，请联系管理员确认", MsgTypeEnum.ERROR);
        }
        log.error("请求地址'{}', Mybatis系统异常", requestUri, e);
        return Response.with(ErrorCodeEnum.DB_TABLE_READ_FAIL, null, ERROR_MSG, MsgTypeEnum.ERROR);
    }

    /**
     * Mybatis系统异常 通用处理
     */
    @ExceptionHandler(MybatisPlusException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<String> handleMybatisPlusException(MybatisPlusException e) {
        log.error("MybatisPlus异常", e);
        return Response.with(ErrorCodeEnum.UNKNOWN, null, ERROR_MSG, MsgTypeEnum.ERROR);
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Response<String> handleGlobalRuntimeException(RuntimeException ex, HandlerMethod handlerMethod) {
        log.error("运行时异常:", ex);
        return Response.with(ErrorCodeEnum.SYS_ERROR, null, ERROR_MSG, MsgTypeEnum.ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Response<String> handleGlobalException(Exception ex) {
        log.error("系统异常:", ex);
        return Response.with(ErrorCodeEnum.SYS_ERROR, null, ERROR_MSG, MsgTypeEnum.ERROR);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<String> validatedBindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
        return Response.with(ErrorCodeEnum.INVALID_PARAMETER, null, fieldErrors.get(0).getDefaultMessage(), MsgTypeEnum.ERROR);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<String> validExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
        return Response.with(ErrorCodeEnum.INVALID_PARAMETER, null, String.format("%s %s", fieldErrors.get(0).getField(), fieldErrors.get(0).getDefaultMessage()), MsgTypeEnum.ERROR);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<String> exceptionHandler(HttpMessageNotReadableException e, HandlerMethod handlerMethod) {
        log.error("HttpMessageNotReadableException:", e);
        log.error("请求参数错误 controller出错，类名：{} 方法名：{}", handlerMethod.getMethod().getDeclaringClass().getName(),
                handlerMethod.getMethod().getName());
        return Response.with(ErrorCodeEnum.INVALID_PARAMETER, null, "convert exception message to JSON", MsgTypeEnum.ERROR);
    }
}
