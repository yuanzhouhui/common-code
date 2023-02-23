package com.bright.commoncode.enums;

/**
 * @author yuanzhouhui
 * @description
 * @date 2023-02-23 14:48
 */
public enum MsgTypeEnum {
    INFO("info"),
    WARN("warn"),
    ERROR("error");

    private String type;

    private MsgTypeEnum(String type) {
        this.type = type;
    }
}
