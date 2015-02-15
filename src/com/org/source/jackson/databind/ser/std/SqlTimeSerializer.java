package com.org.source.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;

import com.org.source.jackson.core.JsonGenerationException;
import com.org.source.jackson.core.JsonGenerator;
import com.org.source.jackson.databind.JavaType;
import com.org.source.jackson.databind.JsonMappingException;
import com.org.source.jackson.databind.JsonNode;
import com.org.source.jackson.databind.SerializerProvider;
import com.org.source.jackson.databind.annotation.JacksonStdImpl;
import com.org.source.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.org.source.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.org.source.jackson.databind.jsonFormatVisitors.JsonValueFormat;

@JacksonStdImpl
public class SqlTimeSerializer
    extends StdScalarSerializer<java.sql.Time>
{
    public SqlTimeSerializer() { super(java.sql.Time.class); }

    @Override
    public void serialize(java.sql.Time value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonGenerationException
    {
        jgen.writeString(value.toString());
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
        JsonStringFormatVisitor v2 = (visitor == null) ? null : visitor.expectStringFormat(typeHint);
        if (v2 != null) {
            v2.format(JsonValueFormat.DATE_TIME);
        }
    }
}