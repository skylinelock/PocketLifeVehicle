package dev.sky_lock.pocketlifevehicle

import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.types.Type
import dev.sky_lock.pocketlifevehicle.vehicle.ModelArmorStand
import dev.sky_lock.pocketlifevehicle.vehicle.SeatArmorStand
import net.minecraft.server.v1_14_R1.*

/**
 * @author sky_lock
 */

class VehicleEntityType<T : Entity>(
    private val key: String,
    private val entityType: EntityTypes<out Entity>,
    private val builder: EntityTypes.b<T>
) {

    companion object {
        val MODEL = VehicleEntityType(
            "vehicle_model",
            EntityTypes.ARMOR_STAND,
            EntityTypes.b<ModelArmorStand> { type, world -> ModelArmorStand(type, world) })
        val SEAT = VehicleEntityType(
            "vehicle_seat",
            EntityTypes.ARMOR_STAND,
            EntityTypes.b<SeatArmorStand> { type, world -> SeatArmorStand(type, world) })

        fun registerTypes() {
            MODEL.register()
            SEAT.register()
        }

        fun unregisterTypes() {
            MODEL.unregister()
            SEAT.unregister()
        }
    }

    private fun register() {
        // https://www.spigotmc.org/threads/registering-custom-entities-in-1-14-2.381499/#post-3460944
        val dataTypes = this.getRegisteredEntityTypesMap()
        val minecraftKey = MinecraftKey.a(this.key) ?: return
        val parentMinecraftKey = MinecraftKey.a(this.entityType.h().key.split("/")[1]) ?: return
        dataTypes[minecraftKey.toString()] = dataTypes[parentMinecraftKey.toString()]

        val entity: EntityTypes.a<Entity> = EntityTypes.a.a(this.builder, EnumCreatureType.MISC)
        IRegistry.a(IRegistry.ENTITY_TYPE, 1, this.key, entity.a(this.key))
    }

    private fun unregister() {
        val minecraftKey = MinecraftKey.a(this.key) ?: return
        val dataTypes = this.getRegisteredEntityTypesMap()
        dataTypes.remove(minecraftKey.toString())
    }

    private fun getRegisteredEntityTypesMap(): MutableMap<String, Type<*>?> {
        return DataConverterRegistry.a()
            .getSchema(DataFixUtils.makeKey(SharedConstants.a().worldVersion))
            .findChoiceType(DataConverterTypes.ENTITY)
            .types() as MutableMap<String, Type<*>?>
    }

    fun <T : Entity> type(): EntityTypes<T> {
        return IRegistry.ENTITY_TYPE.get(MinecraftKey.a(key)) as EntityTypes<T>
    }

}