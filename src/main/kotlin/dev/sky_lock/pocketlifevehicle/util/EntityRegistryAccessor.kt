package dev.sky_lock.pocketlifevehicle.util

import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.types.Type
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

class EntityRegistryAccessor {

    fun <T : Entity, U : T> inject(id: String, entityType: EntityType<T>, builder: EntityType.EntityFactory<U>) {
        // https://www.spigotmc.org/threads/registering-custom-entities-in-1-14-2.381499/#post-3460944
        val dataTypes = this.getRegisteredEntityTypesMap()
        val resourceKey = ResourceLocation.tryParse(id) ?: return
        val parentMinecraftKey =
            ResourceLocation.tryParse(entityType.defaultLootTable.path.split("/")[1]) ?: return
        dataTypes[resourceKey.toString()] = dataTypes[parentMinecraftKey.toString()]

        val entity: EntityType.Builder<Entity> = EntityType.Builder.of(builder, MobCategory.MISC)

        val registry = BuiltInRegistries.ENTITY_TYPE as DefaultedMappedRegistry<*>
        val mappedRegistryClass = registry::class.java.superclass
        val unregisteredIntrusiveHolders = mappedRegistryClass.getDeclaredField("m")
        unregisteredIntrusiveHolders.isAccessible = true
        unregisteredIntrusiveHolders.set(registry, mutableMapOf<Entity, Holder.Reference<U>>())
        val frozen = mappedRegistryClass.getDeclaredField("l")
        frozen.isAccessible = true
        frozen.set(registry, false)

        //Registry.registerMapping(BuiltInRegistries.ENTITY_TYPE, 2, id, entity.build(id))

        unregisteredIntrusiveHolders.set(registry, null)
        unregisteredIntrusiveHolders.isAccessible = false
        frozen.set(registry, true)
        frozen.isAccessible = false
    }

    @Suppress("UNCHECKED_CAST")
    private fun getRegisteredEntityTypesMap(): MutableMap<String, Type<*>?> {
        return DataFixers.getDataFixer()
            .getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().dataVersion.version))
            .findChoiceType(References.ENTITY)
            .types() as MutableMap<String, Type<*>?>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Entity> getType(id: String): EntityType<T> {
        return BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse(id)) as EntityType<T>
    }

}