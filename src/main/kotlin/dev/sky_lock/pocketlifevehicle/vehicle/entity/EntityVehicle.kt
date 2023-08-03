package dev.sky_lock.pocketlifevehicle.vehicle.entity

import dev.sky_lock.pocketlifevehicle.ext.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.packet.AnimationPacket
import dev.sky_lock.pocketlifevehicle.text.Line
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
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
    var boostingTick = false
    var driftLevelUpdated = false
    private var driftedTicks = 0
    private var driftStartingYaw = 0

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

    fun calculateSpeed(sidewaysSpeed: Float, forwardSpeed: Float, spaced: Boolean): Float {
        if (fuel <= ZERO) {
            speedController.zero()
            return speedController.exact()
        }
        if (spaced) {
            if (speedController.isPositive) {
                speedController.sideBreakDecelerate()
            } else {
                speedController.sideBreakAccelerate()
            }
            if (sidewaysSpeed != ZERO) {
                driftedTicks++
                if (driftedTicks == 21 || driftedTicks == 61 || driftedTicks == 81 || driftedTicks == 91) {
                    driftLevelUpdated = true
                }
            }
        } else {
            val driftLevel = calculateDriftLevel()
            if (driftLevel != 0 && driftLevel != 4) {
                speedController.boost(driftLevel)
                boostingTick = true
            }
            driftedTicks = 0
        }
        if (forwardSpeed > ZERO) {
            if (!exceedSpeedMaxValue()) {
                speedController.accelerate()
            }
        }
        if (forwardSpeed == ZERO) {
            if (speedController.isPositive) {
                speedController.frictionalDecelerate()
            }
        }
        if (forwardSpeed < ZERO) {
            speedController.decelerate()
        }
        if (exceedSpeedMaxValue()) {
            speedController.frictionalDecelerate()
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

    private fun exceedSpeedMaxValue(): Boolean {
        return speedController.exact() > model.spec.maxSpeed.value
    }

    fun calculateDriftLevel(): Int {
        return when (driftedTicks) {
            in 0..20 -> 0
            in 21..60 -> 1
            in 61..80 -> 2
            in 81..90 -> 3
            else -> 4
        }
    }

    fun calculateDeltaMovement(sidewaysSpeed: Float, spaced: Boolean): Vec3 {
        if (!spaced) return Vec3(0.0, 0.0, 1.0)

        return Vec3(-sidewaysSpeed.toDouble(), 0.0, 1.0)
    }

    fun updateYaw(driver: Player, sidewaysSpeed: Float) {
        if (fuel.roundToInt() == 0 || speedController.isApproximateZero) {
            return
        }
        val steeringYaw = model.spec.steeringLevel.value
        if (sidewaysSpeed > ZERO) {
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
        } else if (sidewaysSpeed < ZERO) {
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
            line.darkGreenBold(blockPerSecond).grayBold(" blocks/s ")
            val driftLevel = calculateDriftLevel()
            for (i in 0 until driftLevel) {
                when (i) {
                    0 -> {
                        line.darkGreen("●")
                    }
                    1 -> {
                        line.green("●")
                    }
                    2 -> {
                        line.yellow("●")
                    }
                    3 -> {
                        line.red("●")
                    }
                }
            }
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