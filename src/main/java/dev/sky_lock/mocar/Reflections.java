package dev.sky_lock.mocar;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @author sky_lock
 */

public class Reflections {

    public static <T> Field getField(Class<T> clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return field;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Field field, Object object) throws IllegalAccessException {
        Object obj = field.get(object);
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... params) throws NoSuchMethodException {
        Constructor<T> constructor = clazz.getDeclaredConstructor(params);
        constructor.setAccessible(true);
        return constructor;
    }
}
