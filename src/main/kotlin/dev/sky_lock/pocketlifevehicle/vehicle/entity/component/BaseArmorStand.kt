package dev.sky_lock.pocketlifevehicle.vehicle.entity.component

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.level.Level
import org.bukkit.attribute.Attribute
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftArmorStand
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity

/**
 * @author sky_lock
 */

open class BaseArmorStand(type: EntityType<ArmorStand>, level: Level) : ArmorStand(type, level) {

    val craftEntity: CraftArmorStand

    init {
        setDefaultValues()

        super.craftAttributes.registerAttribute(Attribute.GENERIC_MAX_HEALTH)
        super.craftAttributes.registerAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)
        super.craftAttributes.registerAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
        super.craftAttributes.registerAttribute(Attribute.GENERIC_ARMOR)
        super.craftAttributes.registerAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)

        this.craftEntity = CraftArmorStand(this.level().craftServer, this)
    }

    //足音がなるかどうか
    override fun isSilent() = true

    override fun getAttributes(): AttributeMap {
        return AttributeMap(createLivingAttributes().build())
    }

    fun setDependingValue() {
        super.setNoBasePlate(true)
    }

    private fun setDefaultValues() {
        super.setNoGravity(false)
        super.setMarker(false)
        super.setInvulnerable(true)
        super.setInvisible(true)
    }

    override fun getBukkitEntity(): CraftEntity {
        return this.craftEntity
    }

    override fun getType(): EntityType<*> {
        return EntityType.ARMOR_STAND
    }
}