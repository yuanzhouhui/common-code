package com.bright.commoncode.enums;

/**
 * @author yuanzhouhui
 * @date 2023-02-23 14:38
 */
public enum ErrorCodeEnum {
    /**
     *
     */
    SUCCESS(0, "成功"),
    UNKNOWN(1, "未知错误"),
    SYS_ERROR(2, "系统异常"),
    UNSUPPORT(3, "不支持的操作"),
    SERVER_DEVELOPING(4, "当前服务开发中"),
    SERVER_EXCEPTION(5, "服务响应异常，请检查传入参数"),
    INVALID_PARAMETER(100, "请求参数无效"),
    INVALID_API(101, "请求API无效"),
    SESSION_TIMEOUT(102, "会话超时"),
    USER_NOT_LOGIN(103, "用户未登录"),
    INVALID_TOKEN(104, "TOKEN无效"),
    NOT_AUTH_OPTION(105, "请求无操作权限"),
    NOT_EXISTS(200, "指定的对象不存在"),
    ALREADY_EXISTS(201, "指定的对象已存在"),
    NAME_EXISTS(202, "当前对象名称已存在"),
    SYS_EN_NOT_MODIFY(203, "系统英文名称不可变更"),
    NAME_NOT_MODIFY(204, "名称不可变更"),
    NOT_DEL_FAIL(205, "当前对象不可删除"),
    TASK_RUNNING(301, "任务正在调度或运行中"),
    TASK_NOT_RUNNING(302, "任务未运行"),
    TASK_START_FAIL(303, "任务启动失败"),
    TASK_STOP_FAIL(304, "任务停止失败"),
    TASK_INFO_FAIL(305, "任务信息获取失败"),
    DB_CONNECTION_FAIL(401, "数据源连接失败"),
    DB_TABLE_READ_FAIL(402, "表信息读取失败"),
    DB_TABLE_UNSYNC_FAIL(403, "表数据未同步"),
    DB_VERSION_SYNCING(404, "数据源版本正在创建，请等候"),
    DB_VERSION_NOT_DEL(405, "当前版本正在使用中，不可删除"),
    CATALOG_REFRESH_FAIL(406, "数据目录刷新失败"),
    CATALOG_TABLE_MODIFY_FAIL(407, "表信息修改失败"),
    CATALOG_COLUMN_MODIFY_FAIL(408, "字段信息修改失败"),
    FILE_UPLOAD_FAIL(501, "文件上传失败"),
    FILE_NOT_EXISTS(502, "文件不存在"),
    FILE_SAVE_FAIL(503, "文件保存失败"),
    FILE_READ_FAIL(504, "文件读取失败"),
    FILE_TYPE_INVALID(505, "文件类型错误"),
    FILE_NOT_EMPTY(506, "文件不能为空");

    private Integer errorCode;
    private String message;

    private ErrorCodeEnum(Integer errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public Integer getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return this.message;
    }
}
