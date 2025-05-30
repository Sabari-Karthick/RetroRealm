package com.batman.helpers;

import com.batman.exception.wrapper.InternalException;
import com.batman.model.BaseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class CustomRedisJsonSerializer extends GenericJackson2JsonRedisSerializer {

    public CustomRedisJsonSerializer() {
        super(createObjectMapper());
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public byte[] serialize(@Nullable Object value) throws SerializationException {
        log.info("Entering CustomJsonRedisSerializer serialize");

        try {
            if (value == null) return super.serialize(null);

            if (!(value instanceof BaseModel model)) {
                return super.serialize(value);
            }

            Map<String, List<String>> flattenFieldsConfig = model.getFlattenFieldsConfig();
            if (CollectionUtils.isEmpty(flattenFieldsConfig)) {
                return super.serialize(value);
            }

            log.debug("Flattening nested fields with config: {}", flattenFieldsConfig);

            for (Map.Entry<String, List<String>> entry : flattenFieldsConfig.entrySet()) {
                String nestedFieldName = entry.getKey();
                List<String> allowedFields = entry.getValue();

                Field nestedField = getDeclaredFieldRecursive(value.getClass(), nestedFieldName);
                if (nestedField == null) continue;

                nestedField.setAccessible(true);
                Object nestedObject = nestedField.get(value);
                if (nestedObject == null) continue;

                for (Field field : nestedObject.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (!allowedFields.contains(field.getName())) {
                        if (Collection.class.isAssignableFrom(field.getType())) {
                            field.set(nestedObject,getIdList(field));
                        } else {
                            field.set(nestedObject, null);
                        }
                    }
                }
            }

            return super.serialize(model);

        } catch (Exception e) {
            log.error("Error While Serializing for Cache :: {}", ExceptionUtils.getStackTrace(e));
            throw new InternalException("ERROR_WHILE_CACHE_SERIALIZATION");
        } finally {
            log.info("Leaving CustomJsonRedisSerializer serialize");
        }
    }

    private Object getIdList(Field field) {
        return Collections.emptyList();
    }

    private Field getDeclaredFieldRecursive(Class<?> clazz, String fieldName) {
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

}
