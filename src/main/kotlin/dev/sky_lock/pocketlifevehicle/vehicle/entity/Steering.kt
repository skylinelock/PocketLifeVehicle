package dev.sky_lock.pocketlifevehicle.vehicle.entity

import dev.sky_lock.pocketlifevehicle.packet.AnimationPacket
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.player.Player
import kotlin.math.roundToInt

/**
 * @author sky_lock
 */
class Steering(private val status: VehicleStatus) {
    private val spec = status.model.spec

    fun update(driver: Player, sidewaysSpeed: Float) {
        val speed = status.engine.speed
        val tank = status.tank
        if (tank.fuel.roundToInt() == 0 || speed.isApproximateZero) {
            return
        }
        val steeringYaw = spec.steeringLevel.value
        if (sidewaysSpeed > 0.0f) {
            if (speed.isPositive) {
                status.yaw -= steeringYaw
            } else {
                status.yaw += steeringYaw
            }
            if (status.shouldAnimate && status.model.flag.animation) {
                raiseRightArm(driver)
            }
        } else if (sidewaysSpeed < 0.0f) {
            if (speed.isPositive) {
                status.yaw += steeringYaw
            } else {
                status.yaw -= steeringYaw
            }
            if (status.shouldAnimate && status.model.flag.animation) {
                raiseLeftArm(driver)
            }
        }
    }

    private fun raiseLeftArm(player: Player) {
        if (player.mainArm == HumanoidArm.RIGHT) {
            raiseOffhand(player.id)
        } else {
            raiseMainHand(player.id)
        }
    }

    private fun raiseRightArm(player: Player) {
        if (player.mainArm == HumanoidArm.RIGHT) {
            raiseMainHand(player.id)
        } else {
            raiseOffhand(player.id)
        }
    }

    private fun raiseMainHand(entityID: Int) {
        broadcastAnimationPacket(entityID, AnimationPacket.AnimationType.SWING_MAIN_ARM)
    }

    private fun raiseOffhand(entityID: Int) {
        broadcastAnimationPacket(entityID, AnimationPacket.AnimationType.SWING_OFFHAND)
    }

    private fun broadcastAnimationPacket(entityID: Int, type: AnimationPacket.AnimationType) {
        val packet = AnimationPacket()
        packet.setEntityID(entityID)
        packet.setAnimationType(type)
        packet.broadCast()
    }

}