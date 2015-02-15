package com.org.source.jackson.databind.deser.std;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.org.source.jackson.core.JsonParser;
import com.org.source.jackson.databind.DeserializationContext;

public class AtomicBooleanDeserializer extends StdScalarDeserializer<AtomicBoolean>
{
    private static final long serialVersionUID = 1L;

    public AtomicBooleanDeserializer() { super(AtomicBoolean.class); }

    @Override
    public AtomicBoolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return new AtomicBoolean(_parseBooleanPrimitive(jp, ctxt));
    }
}