package com.org.source.dao.base;

import java.lang.reflect.Type;

/**
 * ObjectFactory allows you to instantiate specific types of objects on class types.  This interface allows
 * you to override the default rules.  
 */
public interface ObjectFactory {
    /**
     * This method is called by the deserializer to construct and bind an object.  
     * 
     * @param context the object binding context to keep track of where we are in the object graph
     * and used for binding into objects.
     * @param value This is the value from the json object.
     * @param targetClass concrete class pulled from the configuration of the deserializer.
     *
     * @return the fully bound object.  At the end of this method the object should be fully constructed.
     */
    public Object instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType);
}
