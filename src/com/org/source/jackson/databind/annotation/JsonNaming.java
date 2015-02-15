package com.org.source.jackson.databind.annotation;

import java.lang.annotation.*;

import com.org.source.jackson.databind.PropertyNamingStrategy;

/**
 * Annotation that can be used to indicate a {@link PropertyNamingStrategy}
 * to use for annotated class. Overrides the global (default) strategy.
 * 
 * @since 2.1
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@com.org.source.jackson.annotation.JacksonAnnotation
public @interface JsonNaming
{
    public Class<? extends PropertyNamingStrategy> value();
}
