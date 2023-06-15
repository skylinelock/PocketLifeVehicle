package dev.sky_lock.pocketlifevehicle.vehicle.entity.component

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.level.Level
import org.bukkit.attribute.Attribute

/**
 * @author sky_lock
 */

open class BaseArmorStand(type: EntityType<ArmorStand>, level: Level) : ArmorStand(type, level) {

    init {
        setDefaultValues()

        super.craftAttributes.registerAttribute(Attribute.GENERIC_MAX_HEALTH)
        super.craftAttributes.registerAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)
        super.craftAttributes.registerAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
        super.craftAttributes.registerAttribute(Attribute.GENERIC_ARMOR)
        super.craftAttributes.registerAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)
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

}