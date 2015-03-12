package com.org.source.dao.base;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class BeanAnalyzer {

    private static ThreadLocal<Map<Class<?>,BeanAnalyzer>> sCache = new ThreadLocal<Map<Class<?>,BeanAnalyzer>>();

    private Class<?> mClazz;
    private BeanAnalyzer mSuperBean;
    private Map<String,BeanProperty> mProperties;

    public static BeanAnalyzer analyze(Class<?> clazz ) {
        if(clazz == null) {
            return null;
        }

        if(sCache.get() == null) {
            sCache.set( new HashMap<Class<?>,BeanAnalyzer>() );
        }

        if(!sCache.get().containsKey(clazz)) {
            sCache.get().put(clazz, new BeanAnalyzer(clazz));
        }

        return sCache.get().get( clazz );
    }

    public static void clearCache() {
        sCache.remove();
    }

    protected BeanAnalyzer(Class<?> clazz) {
        this.mClazz = clazz;
        mSuperBean = analyze( clazz.getSuperclass() );
        populateProperties();
    }

    private void populateProperties() {
        mProperties = new TreeMap<String,BeanProperty>();
        for(Method method : mClazz.getDeclaredMethods()) {
            int modifiers = method.getModifiers();
            if(Modifier.isStatic(modifiers)) continue;

            int numberOfArgs = method.getParameterTypes().length;
            String name = method.getName();
            if(name.length() <= 3 && !name.startsWith("is")) continue;
            
            if( numberOfArgs == 0 ) {
                if(name.startsWith("get")) {
                    String property = uncapitalize(name.substring(3));
                    if(!mProperties.containsKey(property)) {
                        mProperties.put(property, new BeanProperty(property, this));
                    }
                    mProperties.get(property).setReadMethod(method);
                } else if(name.startsWith("is")) {
                    String property = uncapitalize(name.substring(2));
                    if( !mProperties.containsKey(property)) {
                        mProperties.put(property, new BeanProperty(property, this));
                    }
                    mProperties.get(property).setReadMethod(method);
                }
            } else if(numberOfArgs == 1) {
                if(name.startsWith("set")) {
                    String property = uncapitalize(name.substring(3));
                    if(!mProperties.containsKey(property)) {
                        mProperties.put( property, new BeanProperty(property, this));
                    }
                    mProperties.get(property).addWriteMethod(method);
                }
            }
        }

        for(Field publicProperties : mClazz.getFields()) {
            int modifiers = publicProperties.getModifiers();
            if(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) continue;
            if(!mProperties.containsKey(publicProperties.getName())) {
                mProperties.put(publicProperties.getName(), new BeanProperty(publicProperties, this));
            }
        }
    }

    public BeanAnalyzer getSuperBean() {
        return mSuperBean;
    }

    private String uncapitalize(String value) {
        if(value.length() < 2) {
             return value.toLowerCase();
        } else if(Character.isUpperCase(value.charAt(0)) && Character.isUpperCase(value.charAt(1))) {
            return value;
        } else {
            return Character.toLowerCase(value.charAt(0)) + value.substring(1);
        }
    }

    public BeanProperty getProperty(String name) {
        BeanAnalyzer current = this;
        while(current != null) {
            BeanProperty property = current.mProperties.get( name );
            if( property != null ) return property;
            current = current.mSuperBean;
        }
        return null;
    }

    public Collection<BeanProperty> getProperties() {
        Map<String,BeanProperty> properties = new TreeMap<String,BeanProperty>(this.mProperties);
        BeanAnalyzer current = this.mSuperBean;
        while(current != null) {
            merge(properties, current.mProperties);
            current = current.mSuperBean;
        }
        return properties.values();
    }

    private void merge(Map<String, BeanProperty> destination, Map<String, BeanProperty> source) {
        for(String key : source.keySet()) {
            if( !destination.containsKey(key) ) {
                destination.put(key, source.get(key));
            }
        }
    }

    public boolean hasProperty(String name) {
        return mProperties.containsKey(name);
    }

    protected Field getDeclaredField(String name) {
        try {
            return mClazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            // ignore field does not exist.
            return null;
        }
    }
}
