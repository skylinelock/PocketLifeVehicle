package dev.sky_lock.mocar;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.util.Reflection;
import net.minecraft.server.v1_13_R2.*;

import java.util.Map;

/**
 * @author sky_lock
 */

public enum EntityRegistry {
    CAR_ARMORSTAND;

    EntityRegistry() {
        register();
    }

    private void register() {
        String carID = "car_armor_stand";
        Object[] typeByID = null;
        Map<Object, Type<?>> types = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(1631)).findChoiceType(DataConverterTypes.n).types();
        types.put("minecraft:" + carID, types.get("minecraft:armor_stand"));

        typeByID = Reflection.getFieldValue(Reflection.getField(RegistryID.class, "d"), Reflection.getFieldValue(Reflection.getField(RegistryMaterials.class, "b"), IRegistry.ENTITY_TYPE));

        Object plainArmorStand = typeByID[1];
        EntityTypes<CarArmorStand> ENTITY_TYPE = EntityTypes.a.a(CarArmorStand.class, CarArmorStand::new).a(carID);
        IRegistry.ENTITY_TYPE.a(1, new MinecraftKey(carID), ENTITY_TYPE);
        typeByID[1] = plainArmorStand;
    }
}
