package com.org.source.jackson.databind.ser.std;

import java.io.File;
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
 * For now, File objects get serialized by just outputting
 * absolute (but not canonical) name as String value
 */
public class FileSerializer
    extends StdScalarSerializer<File>
{
    public FileSerializer() { super(File.class); }

    @Override
    public void serialize(File value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonGenerationException
    {
        jgen.writeString(value.getAbsolutePath());
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