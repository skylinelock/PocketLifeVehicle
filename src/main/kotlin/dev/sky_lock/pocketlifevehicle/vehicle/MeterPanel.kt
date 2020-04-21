package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.extension.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.stream.IntStream
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * @author sky_lock
 */
class MeterPanel(private val status: CarStatus, private val model: Model, private val engine: Engine) {
    fun display(player: Player) {
        val builder = StringBuilder()
        builder.append(ChatColor.RED).append(ChatColor.BOLD).append("E ").append(ChatColor.GREEN)
        val fuelRate = status.fuel / model.spec.maxFuel
        val filled = (70 * fuelRate).roundToInt()
        IntStream.range(0, filled).forEach { builder.append("ǀ") }
        builder.append(ChatColor.RED)
        IntStream.range(0, 70 - filled).forEach { builder.append("ǀ") }
        builder.append(" ").append(ChatColor.GREEN).append(ChatColor.BOLD).append(" F").append("   ").append(ChatColor.DARK_PURPLE).append(ChatColor.BOLD)
        val speed = status.speed
        if (speed.isApproximateZero) {
            builder.append("P")
        } else {
            if (speed.isPositive) {
                builder.append("D")
            } else if (speed.isNegative) {
                builder.append("R")
            }
        }
        builder.append("   ")
        builder.append(ChatColor.DARK_GREEN).append(ChatColor.BOLD)
        val blockPerSecond = abs(engine.speedPerSecond()).truncateToOneDecimalPlace()
        builder.append(blockPerSecond).append(ChatColor.GRAY).append(ChatColor.BOLD).append(" blocks/s")
        player.sendActionBar(builder.toString())
    }

}