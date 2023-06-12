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
                if (driver.mainArm == HumanoidArm.RIGHT) {
                    raiseMainHand(driver.id)
                } else {
                    raiseOffHand(driver.id)
                }
            }
        } else if (sidewaysSpeed < 0.0f) {
            if (speedController.isPositive) {
                location.yaw = location.yaw + steeringYaw
            } else {
                location.yaw = location.yaw - steeringYaw
            }
            if (shouldAnimate && model.flag.animation) {
                if (driver.mainArm == HumanoidArm.RIGHT) {
                    raiseOffHand(driver.id)
                } else {
                    raiseMainHand(driver.id)
                }
            }
        }
    }

    private fun raiseMainHand(entityID: Int) {
        broadcastAnimationPacket(entityID, AnimationPacket.AnimationType.SWING_MAIN_ARM)
    }

    private fun raiseOffHand(entityID: Int) {
        broadcastAnimationPacket(entityID, AnimationPacket.AnimationType.SWING_OFFHAND)
    }

    private fun broadcastAnimationPacket(entityID: Int, type: AnimationPacket.AnimationType) {
        val packet = AnimationPacket()
        packet.setEntityID(entityID)
        packet.setAnimationType(type)
        packet.broadCast()
    }

    fun createMeterPanelLine(): Line {
        val line = Line()

        if (model.flag.eventOnly) {
            val blockPerSecond = abs(speedController.exact() * 20).truncateToOneDecimalPlace()
            line.darkGreenBold(blockPerSecond).grayBold(" blocks/s")
            return line
        }
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
        val blockPerSecond = abs(speedController.exact() * 20).truncateToOneDecimalPlace()
        line.darkGreenBold(blockPerSecond).grayBold(" blocks/s")
        return line
    }

    fun explode() {
        val explosion = FakeExplosionPacket()
        explosion.setX(location.x)
        explosion.setY(location.y)
        explosion.setZ(location.z)
        explosion.setRadius(5f)
        explosion.broadCast()
        location.world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f)
    }

    fun cancelEngineSound() {
        location.getNearbyPlayers(50.0).forEach { player ->
            player.stopSound(Sound.ENTITY_PIG_HURT)
            player.stopSound(Sound.ENTITY_MINECART_RIDING)
            player.stopSound(Sound.ENTITY_PLAYER_BURP)
            player.stopSound(Sound.ENTITY_ENDERMAN_DEATH)
        }
        shouldPlaySound = false
    }

    fun playEngineSound() {
        if (!shouldPlaySound) return
        val speed = speedController.approximate()
        val maxSpeed = model.spec.maxSpeed.value
        val location = location
        val world = location.world
        val pitch = speed / maxSpeed
        world.playSound(location, Sound.ENTITY_PIG_HURT, 0.03f, 0.7f)
        world.playSound(location, Sound.ENTITY_MINECART_RIDING, 0.03f, 0.8f)
        world.playSound(location, Sound.ENTITY_PLAYER_BURP, 0.03f, 0.8f)
        world.playSound(location, Sound.ENTITY_ENDERMAN_DEATH, 0.03f, pitch)
    }
}