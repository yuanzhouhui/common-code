package com.bright.commoncode.anno;

import com.bright.commoncode.config.jackson.SensitiveJsonSerializer;
import com.bright.commoncode.enums.SensitiveStrategyEnum;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * FieldSensitive
 *
 * @author YuanZhouhui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveJsonSerializer.class)
public @interface FieldSensitive {
    /**
     * 内置脱敏策略
     *
     * @return 脱敏策略
     */
    SensitiveStrategyEnum strategy();
}
