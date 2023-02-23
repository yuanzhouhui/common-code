package com.bright.commoncode.config.jackson;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigNumberSerializer
 *
 * @author YuanZhouhui
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        if (ObjectUtil.isNotEmpty(value)) {
            gen.writeString(value.setScale(4, RoundingMode.HALF_UP) + "");
        } else {
            gen.writeString(value + "");
        }
    }
}
