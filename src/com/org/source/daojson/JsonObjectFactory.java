package com.org.source.daojson;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.json.JSONObject;

import android.util.Log;

import flexjson.JSONException;

public class JsonObjectFactory implements ObjectFactory{
    public Object instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType) {
        Object result = null;
        if (value instanceof JSONObject) {
            try {
                Constructor<?> constructor = targetClass.getDeclaredConstructor();
                constructor.setAccessible( true );
                result = constructor.newInstance();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }

            if (null != result) {
                bindJsonObject(context, (JSONObject)value, result, targetType);
            }
        }
        return result;
    }

    private void bindJsonObject(ObjectBinder objectBinder, JSONObject jsonObj, Object target, Type targetType) {
        try {
            BeanAnalyzer analyzer = BeanAnalyzer.analyze(target.getClass());
            for (BeanProperty descriptor : analyzer.getProperties()) {
                Object value = findFieldInJson(jsonObj, descriptor);
                if (value != null) {
                    Method setMethod = descriptor.getWriteMethod();
                    if (setMethod != null) {
                        Type[] types = setMethod.getGenericParameterTypes();
                        if (types.length == 1) {
                            Type paramType = types[0];
                            Object obj = objectBinder.bind(value, resolveParameterizedTypes(paramType, targetType));
                            setMethod.invoke(target, obj);
                        } else {
                            throw new RuntimeException("Expected a single parameter for method " + target.getClass().getName() + "."
                                            + setMethod.getName() + " but got " + types.length);
                        }
                    } else {
                        Field field = descriptor.getProperty();
                        if (field != null) {
                            field.setAccessible(true);
                            field.set(target, objectBinder.bind(value, field.getGenericType()));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not access the no-arg constructor for " + target.getClass().getName(), e);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException("Exception while trying to invoke setter method.", ex);
        }
    }

    private Object findFieldInJson(JSONObject jsonObject, BeanProperty property) {
        String field = property.getName();
        try {
            if (jsonObject.has(field)) {
                Object value = jsonObject.get(field);
                if (null == value) {
                    field = upperCase(field);
                    if (jsonObject.has(field)) {
                        value = jsonObject.get(field);
                    }
                }
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String upperCase(String field) {
        return Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }
    
    private Type resolveParameterizedTypes(Type genericType, Type targetType) {
        if( genericType instanceof Class ) {
            return genericType;
        } else if( genericType instanceof ParameterizedType ) {
            return genericType;
        } else if( genericType instanceof TypeVariable ) {
            return targetType;
        } else if( genericType instanceof WildcardType ) {
            return targetType;
        } else if( genericType instanceof GenericArrayType ) {
            return ((GenericArrayType)genericType).getGenericComponentType();
        } else {
            throw new JSONException(":  Unknown generic type " + genericType + ".");
        }
    }
}