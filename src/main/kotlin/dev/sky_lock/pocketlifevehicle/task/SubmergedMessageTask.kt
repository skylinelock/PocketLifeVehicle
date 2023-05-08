package dev.sky_lock.pocketlifevehicle.task

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedErrorMessage
import dev.sky_lock.pocketlifevehicle.vehicle.Vehicle
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

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
                        VehicleManager.pop(vehicle)
                        cancel()
                        return
                    }
                    count--
                    return
                }
                if (!vehicle.isInWater) {
                    vehicle.passengers.forEach { player -> warning.stop(player) }
                    cancel()
                    return
                }
                if (count != 0) {
                    warning.count = count
                    vehicle.passengers.forEach { player -> warning.send(player) }
                    count--
                    return
                }
                vehicle.owner?.let { ownerUuid ->
                    if (vehicle.passengers.any { player -> player.uniqueId == ownerUuid }) {
                        return@let
                    }
                    val ownPlayer = Bukkit.getPlayer(ownerUuid)
                    ownPlayer?.sendVehiclePrefixedErrorMessage( "乗り物が" + vehicle.passengers[0].name + "の運転によって水没しました")
                }
                VehicleManager.pop(vehicle)
                vehicle.passengers.forEach { player -> warning.stop(player) }
                cancel()
            }
        }.runTaskTimer(VehiclePlugin.instance, 5L, 20L)
    }
}