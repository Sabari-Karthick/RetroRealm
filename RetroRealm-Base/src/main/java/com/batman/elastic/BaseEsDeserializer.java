package com.batman.elastic;

import com.batman.exception.wrapper.InternalException;
import com.batman.model.BaseModel;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.*;
import java.time.temporal.Temporal;
import java.util.*;

public class BaseEsDeserializer<T extends BaseModel> extends StdDeserializer<T> {


    private final Class<T> clazz;

    public BaseEsDeserializer(Class<T> clazz) {
        super(clazz);
        this.clazz = clazz;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JacksonException {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            Map<String, Object> nestedObjects = new HashMap<>();
            while (p.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = p.currentName();
                p.nextToken();
                Object value = p.readValueAs(Object.class);
                boolean flattened = false;
                for (String nestedField : instance.getFlattenFieldsConfig().keySet()) {
                    if (fieldName.startsWith(nestedField + "_")) {
                        String originalField = fieldName.substring(nestedField.length() + 1);
                        Object nestedFieldObject = clazz.getDeclaredField(nestedField).getType().getDeclaredConstructor().newInstance();
                        nestedObjects
                                .computeIfAbsent(nestedField, k -> nestedFieldObject);
                        Field nested = nestedObjects.get(nestedField).getClass().getDeclaredField(originalField);
                        nested.setAccessible(true);
                        writeValueToObject(nested,nestedObjects.get(nestedField),value);
                        flattened = true;
                        break;
                    }
                }
                if (!flattened) {
                    Field normalField = clazz.getDeclaredField(fieldName);
                    normalField.setAccessible(true);
                    writeValueToObject(normalField,instance,value);
                }

                for (Map.Entry<String, Object> entry : nestedObjects.entrySet()) {
                    Field field = clazz.getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    writeValueToObject(field,instance,entry.getValue());
                }

            }
            return instance;
        } catch (Throwable throwable) {
            throw new InternalException(throwable.getMessage());
        }
    }

    private void writeValueToObject(Field field,Object object,Object value) throws IllegalAccessException {
        Class<?> type = field.getType();
        if(Temporal.class.isAssignableFrom(type)){
            field.set(object,parseDateField(type,value.toString()));
        }else if(Collection.class.isAssignableFrom(type)){
            field.set(object,parseCollectionField(type,value));
        }
        else {
            field.set(object,value);
        }
    }

    private Object parseCollectionField(Class<?> type, Object value) {
        if (Set.class.isAssignableFrom(type) && value instanceof List<?>) {
            return new HashSet<>((List<?>) value);
        }
        return value;
    }


    private static Object parseDateField(Class<?> fieldType, String value) {
        if (fieldType.equals(LocalDate.class)) return LocalDate.parse(value);
        if (fieldType.equals(LocalDateTime.class)) return LocalDateTime.parse(value);
        if (fieldType.equals(LocalTime.class)) return LocalTime.parse(value);
        if (fieldType.equals(Year.class)) return Year.parse(value);
        if (fieldType.equals(ZonedDateTime.class)) return ZonedDateTime.parse(value);
        return value;
    }

}
