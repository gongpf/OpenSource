/**
 * 
 */
package com.org.source.jackson.databind.jsonFormatVisitors;

import com.org.source.jackson.databind.SerializerProvider;

/**
 * @author jphelan
 */
public interface JsonFormatVisitorWithSerializerProvider {
    public SerializerProvider getProvider();
    public abstract void setProvider(SerializerProvider provider);
}
