package com.org.source.jackson.databind.jsontype.impl;

import com.org.source.jackson.annotation.JsonTypeInfo;
import com.org.source.jackson.databind.BeanProperty;
import com.org.source.jackson.databind.jsontype.TypeIdResolver;
import com.org.source.jackson.databind.jsontype.TypeSerializer;

public abstract class TypeSerializerBase extends TypeSerializer
{
    protected final TypeIdResolver _idResolver;

    protected final BeanProperty _property;

    protected TypeSerializerBase(TypeIdResolver idRes, BeanProperty property)
    {
        _idResolver = idRes;
        _property = property;
    }

    @Override
    public abstract JsonTypeInfo.As getTypeInclusion();

    @Override
    public String getPropertyName() { return null; }
    
    @Override
    public TypeIdResolver getTypeIdResolver() { return _idResolver; }

    /*
    /**********************************************************
    /* Helper methods for subclasses
    /**********************************************************
     */

    protected String idFromValue(Object value) {
        String id = _idResolver.idFromValue(value);
        if (id == null) {
            String typeDesc = (value == null) ? "NULL" : value.getClass().getName();
            throw new IllegalArgumentException("Can not resolve type id for "
                    +typeDesc+" (using "+_idResolver.getClass().getName()+")");
        }
        return id;
    }

    protected String idFromValueAndType(Object value, Class<?> type) {
        String id = _idResolver.idFromValueAndType(value, type);
        if (id == null) {
            String typeDesc = (value == null) ? "NULL" : value.getClass().getName();
            throw new IllegalArgumentException("Can not resolve type id for "
                    +typeDesc+" (using "+_idResolver.getClass().getName()+")");
        }
        return id;
    }
}
