package com.org.source.daojson;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectBinder {

    private final Map<Class<?>, ObjectFactory> mFactories;

    public ObjectBinder() {
        mFactories = new HashMap<Class<?>,ObjectFactory>();
        mFactories.put( Integer.class, new ObjectFactoryDef.IntegerObjectFactory());
        mFactories.put( int.class, new ObjectFactoryDef.IntegerObjectFactory() );
        mFactories.put( Float.class, new ObjectFactoryDef.FloatObjectFactory() );
        mFactories.put( float.class, new ObjectFactoryDef.FloatObjectFactory() );
        mFactories.put( Double.class, new ObjectFactoryDef.DoubleObjectFactory() );
        mFactories.put( double.class, new ObjectFactoryDef.DoubleObjectFactory() );
        mFactories.put( Short.class, new ObjectFactoryDef.ShortObjectFactory() );
        mFactories.put( short.class, new ObjectFactoryDef.ShortObjectFactory() );
        mFactories.put( Long.class, new ObjectFactoryDef.LongObjectFactory() );
        mFactories.put( long.class, new ObjectFactoryDef.LongObjectFactory() );
        mFactories.put( Boolean.class, new ObjectFactoryDef.BooleanObjectFactory() );
        mFactories.put( boolean.class, new ObjectFactoryDef.BooleanObjectFactory() );
        mFactories.put( Character.class, new ObjectFactoryDef.CharacterObjectFactory() );
        mFactories.put( char.class, new ObjectFactoryDef.CharacterObjectFactory() );
        mFactories.put( String.class, new ObjectFactoryDef.StringObjectFactory() );
        mFactories.put( List.class, new JsonArrayFactory() );
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
        ObjectFactory result = mFactories.get(targetClass);
        if (null == result) {
            result = new JsonObjectFactory();
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
