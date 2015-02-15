package com.org.source.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;


import com.org.source.jackson.core.*;
import com.org.source.jackson.databind.JavaType;
import com.org.source.jackson.databind.JsonMappingException;
import com.org.source.jackson.databind.JsonNode;
import com.org.source.jackson.databind.SerializerProvider;
import com.org.source.jackson.databind.annotation.JacksonStdImpl;
import com.org.source.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

/**
 * This is the special serializer for regular {@link java.lang.String}s.
 *<p>
 * Since this is one of "native" types, no type information is ever
 * included on serialization (unlike for most scalar types as of 1.5)
 */
@JacksonStdImpl
public final class StringSerializer
    extends NonTypedScalarSerializerBase<String>
{
    public StringSerializer() { super(String.class); }

    /**
     * For Strings, both null and Empty String qualify for emptiness.
     */
    @Override
    public boolean isEmpty(String value) {
        return (value == null) || (value.length() == 0);
    }
    
    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value);
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        return createSchemaNode("string", true);
    }
    
    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        if (visitor != null) visitor.expectStringFormat(typeHint);
    }
}
