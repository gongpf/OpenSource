package com.org.source.daojson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class JsonArrayFactory implements ObjectFactory{

    @SuppressWarnings("unchecked")
    @Override
    public Object instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType) {
        if( value instanceof JSONArray) {
            return bindIntoCollection(context, (JSONArray)value, new ArrayList(), targetType);
        }
        return null;
    }
    
    private <T extends Collection<Object>> T bindIntoCollection(ObjectBinder context, JSONArray value, T target, Type targetType) {
        Type valueType = null;
        if( targetType instanceof ParameterizedType) {
            valueType = ((ParameterizedType)targetType).getActualTypeArguments()[0];
        }
        int length = value.length();
        for(int i = 0; i < length; i++) {
            try {
                Object obj = value.get(i);
                target.add(context.bind(obj, valueType));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return target;
    }
}