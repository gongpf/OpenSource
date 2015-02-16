package com.org.source.jackson.databind.deser.impl;

import com.org.source.jackson.annotation.ObjectIdGenerator;
import com.org.source.jackson.annotation.ObjectIdGenerators;

// Simple placeholder
public class PropertyBasedObjectIdGenerator
	extends ObjectIdGenerators.PropertyGenerator
{
    private static final long serialVersionUID = 1L;

    public PropertyBasedObjectIdGenerator(Class<?> scope) {
        super(scope);
    }
    
    @Override
    public Object generateId(Object forPojo) {
    	throw new UnsupportedOperationException();
    }

    @Override
    public ObjectIdGenerator<Object> forScope(Class<?> scope) {
        return (scope == _scope) ? this : new PropertyBasedObjectIdGenerator(scope);
    }

    @Override
    public ObjectIdGenerator<Object> newForSerialization(Object context) {
        return this;
    }

    @Override
    public com.org.source.jackson.annotation.ObjectIdGenerator.IdKey key(Object key) {
        // should we use general type for all; or type of property itself?
        return new IdKey(getClass(), _scope, key);
    }

}