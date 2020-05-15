package dev.sky_lock.pocketlifevehicle

import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.types.Type
import dev.sky_lock.pocketlifevehicle.vehicle.ModelArmorStand
import dev.sky_lock.pocketlifevehicle.vehicle.SeatArmorStand
import net.minecraft.server.v1_14_R1.*

/**
 * @author sky_lock
 */

enum class CustomEntityTypes(val key: String, private val entityType: EntityTypes<out Entity>, private val builder: EntityTypes.b<*>) {
    VEHICLE_MODEL("vehicle_model", EntityTypes.ARMOR_STAND, EntityTypes.b<ModelArmorStand> { type, world -> ModelArmorStand(type, world) }),
    VEHICLE_SEAT("vehicle_seat", EntityTypes.ARMOR_STAND, EntityTypes.b<SeatArmorStand> { type, world -> SeatArmorStand(type, world) });

    fun register() {
        // https://www.spigotmc.org/threads/registering-custom-entities-in-1-14-2.381499/#post-3460944
        val dataTypes = this.getRegisteredEntityTypesMap()
        val minecraftKey = MinecraftKey.a(this.key) ?: return
        val parentMinecraftKey = MinecraftKey.a(this.entityType.h().key.split("/")[1]) ?: return
        dataTypes[minecraftKey.toString()] = dataTypes[parentMinecraftKey.toString()]

        val entity: EntityTypes.a<Entity> = EntityTypes.a.a(this.builder, EnumCreatureType.MISC)
        IRegistry.a(IRegistry.ENTITY_TYPE, 1, this.key, entity.a(this.key))
    }

    fun unregister() {
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

    companion object {

        fun registerEntities() {
            values().forEach { type ->
                type.register()
            }
        }

        fun unregisterEntities() {
            values().forEach { type ->
                type.unregister()
            }
        }
    }
}