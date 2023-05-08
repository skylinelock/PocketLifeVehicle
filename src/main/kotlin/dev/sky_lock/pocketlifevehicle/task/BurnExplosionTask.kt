package dev.sky_lock.pocketlifevehicle.task

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedErrorMessage
import dev.sky_lock.pocketlifevehicle.vehicle.Vehicle
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

/**
 * @author sky_lock
 */
class BurnExplosionTask {
    fun run(vehicle: Vehicle) {
        object : BukkitRunnable() {
            var count = VehiclePlugin.instance.pluginConfiguration.warningCount()
            var warning = BurnExplosionWarning()
            override fun run() {
                if (vehicle.passengers.isEmpty()) {
                    if (count == 0) {
                        vehicle.explode()
                        VehicleManager.remove(vehicle)
                        cancel()
                    }
                    count--
                    return
                }
                if (count == 0) {
                    vehicle.explode()
                    vehicle.owner?.let { ownerUuid ->
                        val owner = Bukkit.getPlayer(ownerUuid) ?: return@let
                        if (vehicle.passengers.any { player: Player -> player.uniqueId == ownerUuid }) {
                            return@let
                        }
                        owner.sendVehiclePrefixedErrorMessage( "乗り物が" + vehicle.passengers[0].name + "の運転によって破壊されました")
                    }
                    vehicle.passengers.forEach { player ->
                        warning.stop(player)
                        ((player as CraftPlayer).handle as net.minecraft.world.entity.player.Player).kill()
                    }
                    VehicleManager.remove(vehicle)
                    cancel()
                    return
                }
                warning.count = count
                vehicle.passengers.forEach { player: Player -> warning.send(player) }
                count--
            }
        }.runTaskTimer(VehiclePlugin.instance, 5L, 20L)
    }
}