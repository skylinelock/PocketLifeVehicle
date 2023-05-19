package dev.sky_lock.pocketlifevehicle.vehicle.entity.nms

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.level.Level
import org.bukkit.attribute.Attribute

/**
 * @author sky_lock
 */

open class BaseArmorStand<T: ArmorStand> : ArmorStand {

    constructor(type: EntityType<ArmorStand>, world: Level) : super(type, world) {
        super.kill()
    }

    constructor(type: EntityType<T>, world: Level, x: Double, y: Double, z: Double) : super(type, world) {
        super.setPos(x, y, z)

        super.setNoGravity(false)
        super.setMarker(false)

        super.setNoBasePlate(true)
        super.setInvulnerable(true)
        super.setInvisible(true)

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

}