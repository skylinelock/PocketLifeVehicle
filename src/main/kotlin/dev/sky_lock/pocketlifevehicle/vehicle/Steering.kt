package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.packet.AnimationPacket
import org.bukkit.entity.Player
import org.bukkit.inventory.MainHand
import kotlin.math.roundToInt

/**
 * @author sky_lock
 */
class Steering(private val state: State) {
    fun right(driver: Player) {
        val speed = state.speed
        if (state.fuel.roundToInt() == 0 || speed.isApproximateZero) {
            return
        }
        if (speed.isPositive) {
            state.yaw = state.yaw + 4.0f
        } else {
            state.yaw = state.yaw - 4.0f
        }
        if (state.isWieldHand) {
            raiseLeftArm(driver)
        }
    }

    fun left(driver: Player) {
        val speed = state.speed
        if (state.fuel.roundToInt() == 0 || speed.isApproximateZero) {
            return
        }
        if (speed.isPositive) {
            state.yaw = state.yaw - 4.0f
        } else {
            state.yaw = state.yaw + 4.0f
        }
        if (state.isWieldHand) {
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