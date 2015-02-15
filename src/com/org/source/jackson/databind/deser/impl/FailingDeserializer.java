package com.org.source.jackson.databind.deser.impl;

import com.org.source.jackson.core.JsonParser;
import com.org.source.jackson.databind.DeserializationContext;
import com.org.source.jackson.databind.JsonMappingException;
import com.org.source.jackson.databind.deser.std.StdDeserializer;

/**
 * Special bogus "serializer" that will throw
 * {@link JsonMappingException} if an attempt is made to deserialize
 * a value. This is used as placeholder to avoid NPEs for uninitialized
 * structured serializers or handlers.
 */
public class FailingDeserializer extends StdDeserializer<Object>
{
    private static final long serialVersionUID = 1L;

    protected final String _message;

    public FailingDeserializer(String m) {
        super(Object.class);
        _message = m;
    }
    
    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws JsonMappingException{
        throw ctxt.mappingException(_message);
    }
}
