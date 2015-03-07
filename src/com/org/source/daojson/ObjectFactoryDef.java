package com.org.source.daojson;

import java.lang.reflect.Type;

public class ObjectFactoryDef {
    
    public static class StringObjectFactory implements ObjectFactory {

        @Override
        public String instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType) {
            return value.toString();
        }
    }
    
    public static class ShortObjectFactory implements ObjectFactory {
        @Override
        public Short instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType) {
            if (value instanceof Number) {
                return ((Number) value).shortValue();
            } else {
                try {
                    return Short.parseShort(value.toString());
                } catch (Exception e) {
                    throw new RuntimeException("Could not convert " + value.toString() + " to Short type");
                }
            }
        }
    }
    
    public static class DoubleObjectFactory implements ObjectFactory {
        @Override
        public Double instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType) {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else {
                try {
                    return Double.parseDouble(value.toString());
                } catch (Exception e) {
                    throw new RuntimeException("Could not convert " + value.toString() + " to Double type");
                }
            }
        }
    }
    
    public static class FloatObjectFactory implements ObjectFactory {
        @Override
        public Float instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType) {
            if (value instanceof Number) {
                return ((Number) value).floatValue();
            } else {
                try {
                    return Float.parseFloat(value.toString());
                } catch (Exception e) {
                    throw new RuntimeException("Could not convert " + value.toString() + " to Float type");
                }
            }
        }
    }
    
    public static class LongObjectFactory implements ObjectFactory {
        @Override
        public Long instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType) {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            } else {
                try {
                    return Long.parseLong(value.toString());
                } catch (Exception e) {
                    throw new RuntimeException("Could not convert " + value.toString() + " to Long type");
                }
            }
        }
    }

    public static class BooleanObjectFactory implements ObjectFactory {
        @Override
        public Boolean instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType) {
            if (value instanceof Boolean) {
                return (Boolean) value;
            } else if (value instanceof String) {
                String str = (String) value;
                if (str.equalsIgnoreCase("true")) {
                    return true;
                } else if (str.equalsIgnoreCase("false")) {
                    return false;
                } else {
                    throw new RuntimeException("The String " + str + " Could not convert to boolean type");
                }
            } else {
                throw new RuntimeException("The object" + value.toString() + " Could not convert to boolean type");
            }
        }
    }
    
    public static class CharacterObjectFactory implements ObjectFactory {
        @Override
        public Character instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType) {
            return value.toString().charAt(0);
        }
    }
    
    public static class IntegerObjectFactory implements ObjectFactory{
        public Integer instantiate(ObjectBinder context, Object value, Class<?> targetClass, Type targetType) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            } else {
                try {
                    return Integer.parseInt(value.toString());
                } catch (Exception e) {
                    throw new RuntimeException("Could not convert " + value.toString() + " to Integer type");
                }
            }
        }
    }
}
