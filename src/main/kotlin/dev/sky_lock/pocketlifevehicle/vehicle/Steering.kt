package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.packet.AnimationPacket
import org.bukkit.entity.Player
import org.bukkit.inventory.MainHand
import kotlin.math.roundToInt

/**
 * @author sky_lock
 */
class Steering(private val status: CarStatus) {
    fun right(driver: Player) {
        val speed = status.speed
        if (status.fuel.roundToInt() == 0 || speed.isApproximateZero) {
            return
        }
        if (speed.isPositive) {
            status.yaw = status.yaw + 4.0f
        } else {
            status.yaw = status.yaw - 4.0f
        }
        if (status.isWieldHand) {
            raiseLeftArm(driver)
        }
    }

    fun left(driver: Player) {
        val speed = status.speed
        if (status.fuel.roundToInt() == 0 || speed.isApproximateZero) {
            return
        }
        if (speed.isPositive) {
            status.yaw = status.yaw - 4.0f
        } else {
            status.yaw = status.yaw + 4.0f
        }
        if (status.isWieldHand) {
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