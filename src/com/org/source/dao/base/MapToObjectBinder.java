package com.org.source.dao.base;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MapToObjectBinder extends ObjectBinder{
    
    public MapToObjectBinder() {
        mFactories.put(Object.class, new ObjectDataToObjectFactory());
        mFactories.put(Array.class, new ArrayDataToArrayFactory());
        mFactories.put(List.class, new ArrayDataToListFactory());
    }
    
    public Object bind(IMapData input, Type targetType) {
        return super.bind(input, targetType);
    }
    
    public static interface IMapData {
        
        public Object getValue(String key);

        public boolean has(String key);
    }
    
    public static interface IArrayData {
        
        public int getLength();
        
        public Object get(int position);
    }
    
    public static class ArrayDataToArrayFactory implements ObjectFactory {

        @Override
        public Object instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType) {
            if (!(value instanceof IArrayData)) {
                return null;
            }
            Class<?> memberClass = targetClass.getComponentType();

            if (memberClass == null) {
                throw new RuntimeException("Missing concrete class for array.  You might require a use() method.");
            }

            IArrayData valueArray = (IArrayData) value;
            int count = valueArray.getLength();

            if (count <= 0) {
                return null;
            }

            Object array = Array.newInstance(memberClass, count);
            for (int i = 0; i < count; i++) {
                Object v = null;
                try {
                    v = context.bind(valueArray.get(i), memberClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Array.set(array, i, v);
            }

            return array;
        }
    }
    
    public static class ArrayDataToListFactory implements ObjectFactory{

        @Override
        public Object instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType) {
            if( value instanceof IArrayData) {
                return bindIntoCollection(context, (IArrayData)value, new ArrayList<Object>(), targetType);
            }
            return null;
        }
        
        private <T extends Collection<Object>> T bindIntoCollection(ObjectBinder context, IArrayData value, T target, Type targetType) {
            Type valueType = null;

            if( targetType instanceof ParameterizedType) {
                valueType = ((ParameterizedType)targetType).getActualTypeArguments()[0];
            }
            
            if (null == valueType) {
                return null;
            }

            int length = value.getLength();
            for (int i = 0; i < length; i++) {
                try {
                    Object obj = value.get(i);
                    target.add(context.bind(obj, valueType));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return target;
        }
    }
    
    public static class ObjectDataToObjectFactory implements ObjectFactory{
        public Object instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType) {
            Object result = null;
            if (value instanceof IMapData) {
                try {
                    Constructor<?> constructor = targetClass.getDeclaredConstructor();
                    constructor.setAccessible( true );
                    result = constructor.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (null != result) {
                    bindJsonObject(context, (IMapData)value, result, targetType);
                }
            }
            return result;
        }

        private void bindJsonObject(ObjectBinder objectBinder, IMapData valueData, Object target, Type targetType) {
            try {
                BeanAnalyzer analyzer = BeanAnalyzer.analyze(target.getClass());
                for (BeanProperty descriptor : analyzer.getProperties()) {
                    Object value = findField(valueData, descriptor);
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

        private Object findField(IMapData value, BeanProperty property) {
            String field = property.getName();
            try {
                if (value.has(field)) {
                    Object valueObj = value.getValue(field);
                    if (null == valueObj) {
                        field = upperCase(field);
                        if (value.has(field)) {
                            valueObj = value.getValue(field);
                        }
                    }
                    return valueObj;
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
                throw new RuntimeException(":  Unknown generic type " + genericType + ".");
            }
        }
    }
}
