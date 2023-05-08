package dev.sky_lock.pocketlifevehicle

import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.types.Type
import dev.sky_lock.pocketlifevehicle.vehicle.ModelArmorStand
import dev.sky_lock.pocketlifevehicle.vehicle.SeatArmorStand
import net.minecraft.SharedConstants
import net.minecraft.core.DefaultedMappedRegistry
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.datafix.DataFixers
import net.minecraft.util.datafix.fixes.References
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

/**
 * @author sky_lock
 */

class VehicleEntityType<T : Entity>(
    private val id: String,
    private val entityType: EntityType<out Entity>,
    private val builder: EntityType.EntityFactory<T>
) {
    companion object {
        val MODEL = VehicleEntityType(
            "vehicle_model",
            EntityType.ARMOR_STAND
        ) { type, world -> ModelArmorStand(type, world) }
        val SEAT = VehicleEntityType(
            "vehicle_seat",
            EntityType.ARMOR_STAND
        ) { type, world -> SeatArmorStand(type, world) }

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
        val resourceKey = ResourceLocation.tryParse(this.id) ?: return
        val parentMinecraftKey = ResourceLocation.tryParse(this.entityType.defaultLootTable.path.split("/")[1]) ?: return
        dataTypes[resourceKey.toString()] = dataTypes[parentMinecraftKey.toString()]

        val entity: EntityType.Builder<Entity> = EntityType.Builder.of(this.builder, MobCategory.MISC)

        val registry = BuiltInRegistries.ENTITY_TYPE as DefaultedMappedRegistry<Entity>
        val mappedRegistryClass = registry::class.java.superclass
        val unregisteredIntrusiveHolders = mappedRegistryClass.getDeclaredField("m")
        unregisteredIntrusiveHolders.isAccessible = true
        unregisteredIntrusiveHolders.set(registry, mutableMapOf<Entity, Holder.Reference<T>>())
        val frozen = mappedRegistryClass.getDeclaredField("l")
        frozen.isAccessible = true
        frozen.set(registry, false)

        Registry.registerMapping(BuiltInRegistries.ENTITY_TYPE, 2, this.id, entity.build(this.id))

        unregisteredIntrusiveHolders.set(registry, null)
        unregisteredIntrusiveHolders.isAccessible = false
        frozen.set(registry, true)
        frozen.isAccessible = false
    }

    private fun unregister() {
        val minecraftKey = ResourceLocation.tryParse(this.id) ?: return
        val dataTypes = this.getRegisteredEntityTypesMap()
        dataTypes.remove(minecraftKey.toString())
    }

    private fun getRegisteredEntityTypesMap(): MutableMap<String, Type<*>?> {
        return DataFixers.getDataFixer()
            .getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().dataVersion.version))
            .findChoiceType(References.ENTITY)
            .types() as MutableMap<String, Type<*>?>
    }

    fun <T : Entity> type(): EntityType<T> {
        return BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse(id)) as EntityType<T>
    }

}