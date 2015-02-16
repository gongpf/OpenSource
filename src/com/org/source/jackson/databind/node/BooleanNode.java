package com.org.source.jackson.databind.node;

import java.io.IOException;

import com.org.source.jackson.core.*;
import com.org.source.jackson.databind.SerializerProvider;


/**
 * This concrete value class is used to contain boolean (true / false)
 * values. Only two instances are ever created, to minimize memory
 * usage.
 */
public class BooleanNode
    extends ValueNode
{
    // // Just need two instances...

    public final static BooleanNode TRUE = new BooleanNode(true);
    public final static BooleanNode FALSE = new BooleanNode(false);

    private final boolean _value;
    
    private BooleanNode(boolean v) { _value = v; }

    public static BooleanNode getTrue() { return TRUE; }
    public static BooleanNode getFalse() { return FALSE; }

    public static BooleanNode valueOf(boolean b) { return b ? TRUE : FALSE; }

    @Override
    public JsonNodeType getNodeType() {
        return JsonNodeType.BOOLEAN;
    }

    @Override public JsonToken asToken() {
        return _value ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE;
    }

    @Override
    public boolean booleanValue() {
        return _value;
    }

    @Override
    public String asText() {
        return _value ? "true" : "false";
    }

    @Override
    public boolean asBoolean() {
        return _value;
    }

    @Override
    public boolean asBoolean(boolean defaultValue) {
        return _value;
    }
    
    @Override
    public int asInt(int defaultValue) {
        return _value ? 1 : 0;
    }
    @Override
    public long asLong(long defaultValue) {
        return _value ? 1L : 0L;
    }
    @Override
    public double asDouble(double defaultValue) {
        return _value ? 1.0 : 0.0;
    }
    
    @Override
    public final void serialize(JsonGenerator jg, SerializerProvider provider)
        throws IOException, JsonProcessingException
    {
        jg.writeBoolean(_value);
    }

    @Override
    public boolean equals(Object o)
    {
        /* 11-Mar-2013, tatu: Apparently ClassLoaders can manage to load
         *    different instances, rendering identity comparisons broken.
         *    So let's use value instead.
         */
        if (o == this) return true;
        if (o == null) return false;
        if (!(o instanceof BooleanNode)) {
            return false;
        }
        return (_value == ((BooleanNode) o)._value);
    }
}