package dev.sky_lock.pocketlifevehicle.task

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendPrefixedPluginMessage
import dev.sky_lock.pocketlifevehicle.vehicle.Vehicle
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager.getOwner
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager.kill
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Consumer

/**
 * @author sky_lock
 */
class SubmergedMessageTask {
    fun run(vehicle: Vehicle) {
        val warning = SubmergedWarning()
        object : BukkitRunnable() {
            var count = VehiclePlugin.instance.pluginConfiguration.warningCount()
            override fun run() {
                if (vehicle.passengers.isEmpty()) {
                    if (count == 0) {
                        kill(vehicle)
                        cancel()
                        return
                    }
                    count--
                    return
                }
                if (!vehicle.isInWater) {
                    vehicle.passengers.forEach(Consumer { player: Player? -> warning.stop(player!!) })
                    cancel()
                    return
                }
                if (count != 0) {
                    warning.count = count
                    vehicle.passengers.forEach(Consumer { player: Player -> warning.send(player) })
                    count--
                    return
                }
                getOwner(vehicle)?.let { ownerUuid ->
                    if (vehicle.passengers.any { player: Player -> player.uniqueId == ownerUuid }) {
                        return@let
                    }
                    val ownPlayer = Bukkit.getPlayer(ownerUuid)
                    ownPlayer?.sendPrefixedPluginMessage(ChatColor.RED + "乗り物が" + vehicle.passengers[0].name + "の運転によって破壊されました")
                }
                kill(vehicle)
                vehicle.passengers.forEach(Consumer { player: Player? -> warning.stop(player!!) })
                cancel()
            }
        }.runTaskTimer(VehiclePlugin.instance, 5L, 20L)
    }
}