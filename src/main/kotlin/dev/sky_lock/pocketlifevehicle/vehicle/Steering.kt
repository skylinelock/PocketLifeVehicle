package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.packet.AnimationPacket
import org.bukkit.entity.Player
import org.bukkit.inventory.MainHand
import kotlin.math.roundToInt

/**
 * @author sky_lock
 */
class Steering(private val vehicle: Vehicle) {
    private val spec = vehicle.model.spec

    fun right(driver: Player) {
        val speed = vehicle.engine.speed
        val tank = vehicle.tank
        if (tank.fuel.roundToInt() == 0 || speed.isApproximateZero) {
            return
        }
        val steeringYaw = spec.steeringLevel.value
        if (speed.isPositive) {
            vehicle.yaw += steeringYaw
        } else {
            vehicle.yaw -= steeringYaw
        }
        if (vehicle.shouldAnimate && vehicle.model.flag.animation) {
            raiseLeftArm(driver)
        }
    }

    fun left(driver: Player) {
        val speed = vehicle.engine.speed
        val tank = vehicle.tank
        if (tank.fuel.roundToInt() == 0 || speed.isApproximateZero) {
            return
        }
        val steeringYaw = spec.steeringLevel.value
        if (speed.isPositive) {
            vehicle.yaw -= steeringYaw
        } else {
            vehicle.yaw += steeringYaw
        }
        if (vehicle.shouldAnimate && vehicle.model.flag.animation) {
            raiseRightArm(driver)
        }
    }

    private fun raiseLeftArm(player: Player) {
        if (player.mainHand == MainHand.RIGHT) {
            raiseOffhand(player.entityId)
        } else {
            raiseMainHand(player.entityId)
        }
    }

    private fun raiseRightArm(player: Player) {
        if (player.mainHand == MainHand.RIGHT) {
            raiseMainHand(player.entityId)
        } else {
            raiseOffhand(player.entityId)
        }
    }

    private fun raiseMainHand(entityID: Int) {
        val packet = AnimationPacket()
        packet.setEntityID(entityID)
        packet.setAnimationType(AnimationPacket.AnimationType.SWING_MAIN_ARM)
        packet.broadCast()
    }

    private fun raiseOffhand(entityID: Int) {
        val packet = AnimationPacket()
        packet.setEntityID(entityID)
        packet.setAnimationType(AnimationPacket.AnimationType.SWING_OFFHAND)
        packet.broadCast()
    }

}