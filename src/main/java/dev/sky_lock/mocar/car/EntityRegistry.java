package dev.sky_lock.mocar.car;

import net.minecraft.server.v1_12_R1.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author sky_lock
 */

public enum EntityRegistry {
    VEHICLE_CAR("armor_stand", 30, CarEntity.class, "Car");

    private EntityRegistry(String entity_id, int id, Class<? extends Entity> clazz, String name) {
        addToMaps(clazz, name, entity_id, id);
    }

    private static void addToMaps(Class clazz, String name, String entity_id, int id) {
        try {
            Method method = net.minecraft.server.v1_12_R1.EntityTypes.class.getDeclaredMethod("a", int.class, String.class, Class.class, String.class);
            method.setAccessible(true);
            method.invoke(net.minecraft.server.v1_12_R1.EntityTypes.class, id, entity_id, clazz, name);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}

