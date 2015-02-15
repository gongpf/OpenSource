package com.org.source.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;

import com.org.source.jackson.core.JsonGenerationException;
import com.org.source.jackson.core.JsonGenerator;
import com.org.source.jackson.databind.JavaType;
import com.org.source.jackson.databind.JsonMappingException;
import com.org.source.jackson.databind.JsonNode;
import com.org.source.jackson.databind.SerializerProvider;
import com.org.source.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

/**
 * Also: default bean access will not do much good with Class.class. But
 * we can just serialize the class name and that should be enough.
 */
public class ClassSerializer
    extends StdScalarSerializer<Class<?>>
{
    public ClassSerializer() { super(Class.class, false); }

    @Override
    public void serialize(Class<?> value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonGenerationException
    {
        jgen.writeString(value.getName());
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint)
    {
        return createSchemaNode("string", true);
    }
    
    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
            throws JsonMappingException
    {
        visitor.expectStringFormat(typeHint);
    }
}