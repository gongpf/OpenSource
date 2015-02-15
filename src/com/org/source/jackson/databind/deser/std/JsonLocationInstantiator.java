package com.org.source.jackson.databind.deser.std;

import com.org.source.jackson.core.JsonLocation;
import com.org.source.jackson.databind.DeserializationConfig;
import com.org.source.jackson.databind.DeserializationContext;
import com.org.source.jackson.databind.JavaType;
import com.org.source.jackson.databind.PropertyMetadata;
import com.org.source.jackson.databind.PropertyName;
import com.org.source.jackson.databind.deser.CreatorProperty;
import com.org.source.jackson.databind.deser.ValueInstantiator;

/**
 * For {@link JsonLocation}, we should be able to just implement
 * {@link ValueInstantiator} (not that explicit one would be very
 * hard but...)
 */
public class JsonLocationInstantiator extends ValueInstantiator
{
    @Override
    public String getValueTypeDesc() {
        return JsonLocation.class.getName();
    }
    
    @Override
    public boolean canCreateFromObjectWith() { return true; }
    
    @Override
    public CreatorProperty[] getFromObjectArguments(DeserializationConfig config) {
        JavaType intType = config.constructType(Integer.TYPE);
        JavaType longType = config.constructType(Long.TYPE);
        return  new CreatorProperty[] {
                creatorProp("sourceRef", config.constructType(Object.class), 0),
                creatorProp("byteOffset", longType, 1),
                creatorProp("charOffset", longType, 2),
                creatorProp("lineNr", intType, 3),
                creatorProp("columnNr", intType, 4)
        };
    }

    private static CreatorProperty creatorProp(String name, JavaType type, int index) {
        return new CreatorProperty(new PropertyName(name), type, null,
                null, null, null, index, null, PropertyMetadata.STD_REQUIRED);
    }
    
    @Override
    public Object createFromObjectWith(DeserializationContext ctxt, Object[] args) {
        return new JsonLocation(args[0], _long(args[1]), _long(args[2]),
                _int(args[3]), _int(args[4]));
    }

    private final static long _long(Object o) {
        return (o == null) ? 0L : ((Number) o).longValue();
    }

    private final static int _int(Object o) {
        return (o == null) ? 0 : ((Number) o).intValue();
    }
}