package com.fine.friendlycc.data.typeadapter;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Int转Boolean
 *
 * @author wulei
 */
public class BooleanTypeAdapter extends TypeAdapter<Boolean> {

    @Override
    public void write(JsonWriter out, Boolean value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }

    @Override
    public Boolean read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case BOOLEAN:
                return in.nextBoolean();
            case NULL:
                in.nextNull();
                return false;
            case NUMBER:
                return in.nextInt() == 1;
            case STRING:
                return Boolean.valueOf(in.nextString());
            default:
                throw new JsonParseException("Expected NUMBER or BOOLEAN but was " + peek);
        }
    }
}
