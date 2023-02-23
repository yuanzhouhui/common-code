package com.bright.commoncode.config.jackson;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bright.commoncode.anno.FieldSensitive;
import com.bright.commoncode.enums.SensitiveStrategyEnum;
import com.bright.commoncode.service.SensitiveService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * SensitiveJsonSerializer
 *
 * @author YuanZhouhui
 */
public class SensitiveJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private SensitiveStrategyEnum strategy;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        SensitiveService sensitiveService = SpringUtil.getBean(SensitiveService.class);
        if (ObjectUtil.isNotEmpty(sensitiveService) && sensitiveService.isSensitive()) {
            gen.writeString(strategy.desensitizer().apply(value));
        } else {
            gen.writeString(value);
        }

    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        FieldSensitive annotation = property.getAnnotation(FieldSensitive.class);

        if (Objects.nonNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            this.strategy = annotation.strategy();
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
