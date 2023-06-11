package dev.sky_lock.pocketlifevehicle.vehicle.entity

import dev.sky_lock.pocketlifevehicle.ext.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.packet.AnimationPacket
import dev.sky_lock.pocketlifevehicle.packet.FakeExplosionPacket
import dev.sky_lock.pocketlifevehicle.text.Line
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.player.Player
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import java.util.*
import java.util.stream.IntStream
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * @author sky_lock
 */

class EntityVehicle(var model: Model, var owner: UUID?, var location: Location) {
    companion object {
        const val ZERO = 0.0F
    }

    var uuid: UUID? = null
    val seats = mutableListOf<UUID>()

    var driver: UUID? = null
    var fuel = ZERO
    var isScrapped = false
    var shouldAnimate = false
    var shouldPlaySound = false
    var isLocked = false
    val speedController = SpeedController()

    val ownerName: String
        get() {
            val uuid = owner ?: return "unknown"
            return Bukkit.getOfflinePlayer(uuid).name ?: "unknown"
        }

    fun isEventOnly(): Boolean {
        return owner == null
    }

    fun registerSeat(uuid: UUID) {
        seats.add(uuid)
    }

    private fun consumeFuel() {
        if (fuel > 0.05F) {
            fuel -= 0.05F
        } else {
            fuel = ZERO
        }
    }

    fun refuel(diff: Float): Boolean {
        val max = model.spec.maxFuel
        if (fuel >= max) {
            return false
        } else if (fuel + diff >= max) {
            fuel = max
            return true
        }
        fuel += diff
        return true
    }

    fun calculateSpeed(sidewaysSpeed: Float, forwardSpeed: Float): Float {
        if (fuel <= ZERO) {
            speedController.zero()
            return speedController.exact()
        }
        if (speedController.exact() > model.spec.maxSpeed.value) {
            if (forwardSpeed < ZERO) {
                speedController.decelerate()
            } else if (forwardSpeed == ZERO) {
                speedController.frictionalDecelerate()
            }
            if (!model.flag.eventOnly && model.flag.consumeFuel) {
                consumeFuel()
            }
            return speedController.exact()
        }
        if (forwardSpeed == ZERO) {
            if (speedController.isPositive) {
                speedController.frictionalDecelerate()
            }
        } else if (forwardSpeed < ZERO) {
            speedController.decelerate()
        } else {
            speedController.accelerate()
        }
        if (speedController.isNegative) {
            speedController.decrease()
        }
        if (speedController.approximate() == ZERO && sidewaysSpeed == ZERO) {
            return speedController.exact()
        }
        if (!model.flag.eventOnly && model.flag.consumeFuel) {
            consumeFuel()
        }
        return speedController.exact()
    }

    fun updateYaw(driver: Player, sidewaysSpeed: Float) {
        if (fuel.roundToInt() == 0 || speedController.isApproximateZero) {
            return
        }
        val steeringYaw = model.spec.steeringLevel.value
        if (sidewaysSpeed > 0.0f) {
            if (speedController.isPositive) {
                location.yaw = location.yaw - steeringYaw
            } else {
                location.yaw = location.yaw + steeringYaw
            }
            if (shouldAnimate && model.flag.animation) {
                raiseRightArm(driver)
            }
        } else if (sidewaysSpeed < 0.0f) {
            if (speedController.isPositive) {
                location.yaw = location.yaw + steeringYaw
            } else {
                location.yaw = location.yaw - steeringYaw
            }
            if (shouldAnimate && model.flag.animation) {
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

    fun meterPanelLine(): Line {
        val line = Line()

        if (!model.flag.eventOnly) {
            var fuelRate = fuel / model.spec.maxFuel
            if (fuelRate > 1.0F) {
                fuelRate = 1.0F
                fuel = model.spec.maxFuel
            }
            val filled = (70 * fuelRate).roundToInt()

            line.redBold("E ")
            IntStream.range(0, filled).forEach { line.green("ǀ") }
            IntStream.range(0, 70 - filled).forEach { line.red("ǀ") }
            line.greenBold(" F   ")
            if (speedController.isApproximateZero) {
                line.darkPurpleBold("P   ")
            } else {
                if (speedController.isPositive) {
                    line.darkPurpleBold("D   ")
                } else if (speedController.isNegative) {
                    line.darkPurpleBold("R   ")
                }
            }
        }
        val blockPerSecond = abs(speedController.exact() * 20).truncateToOneDecimalPlace()
        line.darkGreenBold(blockPerSecond).grayBold(" blocks/s")
        return line
    }

    fun playExplosionEffect() {
        val explosion = FakeExplosionPacket()
        explosion.setX(location.x)
        explosion.setY(location.y)
        explosion.setZ(location.z)
        explosion.setRadius(5f)
        explosion.broadCast()
        location.world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f)
    }
}