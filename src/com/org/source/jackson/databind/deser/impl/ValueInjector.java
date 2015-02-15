package com.org.source.jackson.databind.deser.impl;

import java.io.IOException;

import com.org.source.jackson.databind.BeanProperty;
import com.org.source.jackson.databind.DeserializationContext;
import com.org.source.jackson.databind.JavaType;
import com.org.source.jackson.databind.PropertyMetadata;
import com.org.source.jackson.databind.PropertyName;
import com.org.source.jackson.databind.introspect.AnnotatedMember;
import com.org.source.jackson.databind.util.Annotations;

/**
 * Class that encapsulates details of value injection that occurs before
 * deserialization of a POJO. Details include information needed to find
 * injectable value (logical id) as well as method used for assigning
 * value (setter or field)
 */
public class ValueInjector
    extends BeanProperty.Std
{
    /**
     * Identifier used for looking up value to inject
     */
    protected final Object _valueId;

    public ValueInjector(PropertyName propName, JavaType type,
            Annotations contextAnnotations, AnnotatedMember mutator,
            Object valueId)
    {
        super(propName, type, null, contextAnnotations, mutator,
                PropertyMetadata.STD_OPTIONAL);
        _valueId = valueId;
    }

    @Deprecated // since 2.3
    public ValueInjector(String propName, JavaType type,
            Annotations contextAnnotations, AnnotatedMember mutator,
            Object valueId)
    {
        this(new PropertyName(propName), type, contextAnnotations, mutator, valueId);
    }

    public Object findValue(DeserializationContext context, Object beanInstance)
    {
        return context.findInjectableValue(_valueId, this, beanInstance);
    }
    
    public void inject(DeserializationContext context, Object beanInstance)
        throws IOException
    {
        _member.setValue(beanInstance, findValue(context, beanInstance));
    }
}