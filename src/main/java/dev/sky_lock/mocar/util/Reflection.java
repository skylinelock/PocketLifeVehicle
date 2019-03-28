package dev.sky_lock.mocar.util;

import java.lang.reflect.Field;

/**
 * @author sky_lock
 */

public class Reflection {

    public static <T> Field getField(Class<T> clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }
        return field;
    }

    public static <T> T getFieldValue(Field field, Object object) {
        Object obj = null;
        try {
            obj = field.get(object);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return (T) obj;
    }
}
