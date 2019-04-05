package dev.sky_lock.mocar.car;

import com.mojang.datafixers.types.Type;
import net.minecraft.server.v1_13_R2.*;

import java.util.Map;
import java.util.function.Function;

/**
 * @author sky_lock
 */

public enum EntityRegistry {
    CAR_ARMORSTAND("car_armor_stand", CarArmorStand.class, CarArmorStand::new),
    SEAT_ARMORSTAND("seat_armor_stand", SeatArmorStand.class, SeatArmorStand::new);

    <T extends Entity> EntityRegistry(String id, Class<? extends T> clazz, Function<World, ? extends T> function) {
        register(id, clazz, function);
    }

    private <T extends Entity> void register(String id, Class<? extends T> clazz, Function<World, ? extends T> function) {
        /*Object[] typeByID = null;
        Map<Object, Type<?>> types = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(1631)).findChoiceType(DataConverterTypes.n).types();
        types.put("minecraft:" + id, types.of("minecraft:armor_stand"));

        typeByID = Reflection.getFieldValue(Reflection.getField(RegistryID.class, "d"), Reflection.getFieldValue(Reflection.getField(RegistryMaterials.class, "b"), IRegistry.ENTITY_TYPE));

        Object plainArmorStand = typeByID[1];
        EntityTypes<T> ENTITY_TYPE = EntityTypes.a.a(clazz, function).a(id);
        IRegistry.ENTITY_TYPE.a(1, new MinecraftKey(id), ENTITY_TYPE);
        typeByID[1] = plainArmorStand;*/
        Map<Object, Type<?>> types = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(15190).findChoiceType(DataConverterTypes.n).types();
        types.put("minecraft:" + id, types.get("minecraft:armor_stand"));

        System.out.println(EntityTypes.a(id, EntityTypes.a.a(clazz, function)).toString());
    }
}
