package dev.sky_lock.pocketlifevehicle.vehicle.entity.component

import dev.sky_lock.pocketlifevehicle.util.EntityRegistryAccessor
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.decoration.ArmorStand

/**
 * @author sky_lock
 */

object Components {

    private const val MODEL_ID = "vehicle_model"
    private const val SEAT_ID = "vehicle_seat"

    private val accessor = EntityRegistryAccessor()

    fun registerEntityTypes() {
        accessor.inject(
            MODEL_ID,
            EntityType.ARMOR_STAND
        ) { type, world -> ModelArmorStand(type, world) }
        accessor.inject(
            SEAT_ID,
            EntityType.ARMOR_STAND
        ) { type, world -> SeatArmorStand(type, world) }
    }

    fun getModelEntityType(): EntityType<ArmorStand> {
        return accessor.getType(MODEL_ID)
    }

    fun getSeatEntityType(): EntityType<ArmorStand> {
        return accessor.getType(SEAT_ID)
    }

}