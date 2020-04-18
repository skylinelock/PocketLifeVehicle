package dev.sky_lock.pocketlifevehicle.task

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.extension.plus
import dev.sky_lock.pocketlifevehicle.extension.sendPrefixedPluginMessage
import dev.sky_lock.pocketlifevehicle.vehicle.Car
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.getOwner
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.kill
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
    fun run(car: Car) {
        object : BukkitRunnable() {
            var count = PLVehicle.instance.pluginConfiguration.getWarningCount()
            var warning = BurnExplosionWarning()
            override fun run() {
                if (car.passengers.isEmpty()) {
                    if (count == 0) {
                        car.explode()
                        kill(car)
                        cancel()
                    }
                    count--
                    return
                }
                if (count == 0) {
                    car.explode()
                    getOwner(car)?.let { ownerUuid ->
                        val owner = Bukkit.getPlayer(ownerUuid) ?: return@let
                        if (car.passengers.any { player: Player -> player.uniqueId == ownerUuid }) {
                            return@let
                        }
                        owner.sendPrefixedPluginMessage(ChatColor.RED + "乗り物が" + car.passengers[0].name + "の運転によって破壊されました")
                    }
                    car.passengers.forEach(Consumer { player: Player ->
                        warning.stop(player)
                        ((player as CraftPlayer).handle as EntityPlayer).killEntity()
                    })
                    kill(car)
                    cancel()
                    return
                }
                warning.count = count
                car.passengers.forEach(Consumer { player: Player -> warning.send(player) })
                count--
            }
        }.runTaskTimer(PLVehicle.instance, 5L, 20L)
    }
}