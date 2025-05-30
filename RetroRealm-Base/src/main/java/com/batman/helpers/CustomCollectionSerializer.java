package com.batman.helpers;

import com.batman.model.BaseModel;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CustomCollectionSerializer<T extends BaseModel> extends StdSerializer<Iterable<T>> {


    public CustomCollectionSerializer() {
        this(null);
    }

    protected CustomCollectionSerializer(Class<Iterable<T>> t) {
        super(t);
    }

    @Override
    public void serialize(Iterable<T> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        List<Object> ids = new ArrayList<>();
        for (T t : value) {
            Object primaryKeyValue = t.getPrimaryKeyValue();
            ids.add(primaryKeyValue);
        }
        gen.writeObject(ids);
    }
}

