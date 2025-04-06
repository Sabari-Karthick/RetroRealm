package com.batman.elastic;

import com.batman.model.BaseModel;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BaseEsSerializer<T extends BaseModel> extends StdSerializer<T> {

    public BaseEsSerializer(Class<T> tClass) {
        super(tClass);
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        Map<String, List<String>> flattenFieldsConfig = value.getFlattenFieldsConfig();
        Class<? extends BaseModel> valueClass = value.getClass();
        for (Field field : valueClass.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(value);
                if (flattenFieldsConfig.containsKey(field.getName())) {
                    if (Objects.nonNull(fieldValue)) {
                        for (String nestedFieldName : flattenFieldsConfig.get(field.getName())) {
                            Field nestedField = fieldValue.getClass().getDeclaredField(nestedFieldName);
                            nestedField.setAccessible(true);
                            Object nestedFieldValue = nestedField.get(fieldValue);
                            writeValue(gen, field.getName() + "_" + nestedFieldName, nestedFieldValue);
                        }
                    }
                } else {
                    writeValue(gen, field.getName(), fieldValue);
                }
            } catch (Exception exception) {
                ExceptionUtils.printRootCauseStackTrace(exception);
            }

        }

    }

    private void writeValue(JsonGenerator gen, String fieldName, Object value) throws IOException {
        if (value instanceof Temporal) {
            gen.writeObjectField(fieldName, value.toString());
        } else {
            gen.writeObjectField(fieldName, value);
        }
    }
}
