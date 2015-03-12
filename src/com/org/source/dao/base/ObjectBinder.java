package com.org.source.dao.base;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;

public class ObjectBinder {

    protected final Map<Class<?>, ObjectFactory> mFactories;

    public ObjectBinder() {
        mFactories = new HashMap<Class<?>,ObjectFactory>();
        mFactories.put( Integer.class, new DefaultObjectFactorySet.IntegerObjectFactory());
        mFactories.put( int.class, new DefaultObjectFactorySet.IntegerObjectFactory() );
        mFactories.put( Float.class, new DefaultObjectFactorySet.FloatObjectFactory() );
        mFactories.put( float.class, new DefaultObjectFactorySet.FloatObjectFactory() );
        mFactories.put( Double.class, new DefaultObjectFactorySet.DoubleObjectFactory() );
        mFactories.put( double.class, new DefaultObjectFactorySet.DoubleObjectFactory() );
        mFactories.put( Short.class, new DefaultObjectFactorySet.ShortObjectFactory() );
        mFactories.put( short.class, new DefaultObjectFactorySet.ShortObjectFactory() );
        mFactories.put( Long.class, new DefaultObjectFactorySet.LongObjectFactory() );
        mFactories.put( long.class, new DefaultObjectFactorySet.LongObjectFactory() );
        mFactories.put( Boolean.class, new DefaultObjectFactorySet.BooleanObjectFactory() );
        mFactories.put( boolean.class, new DefaultObjectFactorySet.BooleanObjectFactory() );
        mFactories.put( Character.class, new DefaultObjectFactorySet.CharacterObjectFactory() );
        mFactories.put( char.class, new DefaultObjectFactorySet.CharacterObjectFactory() );
        mFactories.put( String.class, new DefaultObjectFactorySet.StringObjectFactory() );
    }
    
    public void addObjectFactory(Class<?> targetClass, ObjectFactory objectFactory) {
        mFactories.put(targetClass, objectFactory);
    }

    public Object bind(Object input, Type targetType) {
        if (input == null) {
            return null;
        }

        Class<?> targetClass = getTargetClass(targetType);

        if (targetClass == null) {
            throw new RuntimeException("Could not find a target Class for " + targetType);
        }

        ObjectFactory factory = findFactory(targetClass);

        if (factory == null) {
            throw new RuntimeException("Could not find a suitable ObjectFactory for " + targetType);
        }
        return factory.instantiate(this, input, targetClass, targetType);
    }
    
    private ObjectFactory findFactory(Class<?> targetClass) {
        ObjectFactory result = targetClass.isArray() ? mFactories.get(Array.class) : mFactories.get(targetClass);
        if (null == result && targetClass != null && targetClass.getSuperclass() != null) {
            result = findFactory(targetClass.getSuperclass());
        }
        return result;
    }

    private Class<?> getTargetClass(Type targetType) {
        if( targetType == null ) {
            return null;
        } else if( targetType instanceof Class ) {
            return (Class<?>)targetType;
        } else if( targetType instanceof ParameterizedType ) {
            return (Class<?>)((ParameterizedType)targetType).getRawType();
        } else if( targetType instanceof GenericArrayType ) {
            return Array.class;
        } else if( targetType instanceof WildcardType ) {
            return null; // nothing you can do about these.  User will have to specify this with use()
        } else if( targetType instanceof TypeVariable ) {
            return null; // nothing you can do about these.  User will have to specify this with use()
        } else {
            throw new RuntimeException("Unknown type " + targetType);
        }
    }
}
