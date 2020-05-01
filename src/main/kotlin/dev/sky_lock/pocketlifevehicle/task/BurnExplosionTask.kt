package dev.sky_lock.pocketlifevehicle.task

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendPrefixedPluginMessage
import dev.sky_lock.pocketlifevehicle.vehicle.Vehicle
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities.getOwner
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities.kill
import net.minecraft.server.v1_14_R1.EntityPlayer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.function.Consumer

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
                        kill(vehicle)
                        cancel()
                    }
                    count--
                    return
                }
                if (count == 0) {
                    vehicle.explode()
                    getOwner(vehicle)?.let { ownerUuid ->
                        val owner = Bukkit.getPlayer(ownerUuid) ?: return@let
                        if (vehicle.passengers.any { player: Player -> player.uniqueId == ownerUuid }) {
                            return@let
                        }
                        owner.sendPrefixedPluginMessage(ChatColor.RED + "乗り物が" + vehicle.passengers[0].name + "の運転によって破壊されました")
                    }
                    vehicle.passengers.forEach(Consumer { player: Player ->
                        warning.stop(player)
                        ((player as CraftPlayer).handle as EntityPlayer).killEntity()
                    })
                    kill(vehicle)
                    cancel()
                    return
                }
                warning.count = count
                vehicle.passengers.forEach(Consumer { player: Player -> warning.send(player) })
                count--
            }
        }.runTaskTimer(VehiclePlugin.instance, 5L, 20L)
    }
}