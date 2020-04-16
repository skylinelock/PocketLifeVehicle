package dev.sky_lock.pocketlifevehicle.task

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.vehicle.Car
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.getOwner
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.kill
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.function.Consumer

/**
 * @author sky_lock
 */
class SubmergedMessageTask {
    fun run(car: Car) {
        val warning = SubmergedWarning()
        object : BukkitRunnable() {
            var count = PLVehicle.instance.pluginConfiguration.getWarningCount()
            override fun run() {
                if (car.passengers.isEmpty()) {
                    if (count == 0) {
                        kill(car)
                        cancel()
                        return
                    }
                    count--
                    return
                }
                if (!car.isInWater) {
                    car.passengers.forEach(Consumer { player: Player? -> warning.stop(player!!) })
                    cancel()
                    return
                }
                if (count != 0) {
                    warning.count = count
                    car.passengers.forEach(Consumer { player: Player -> warning.send(player) })
                    count--
                    return
                }
                getOwner(car).ifPresent { ownerUuid: UUID ->
                    if (car.passengers.stream().anyMatch { player: Player -> player.uniqueId == ownerUuid }) {
                        return@ifPresent
                    }
                    val ownPlayer = Bukkit.getPlayer(ownerUuid)
                    ownPlayer?.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "所有する車が" + car.passengers[0].name + "の運転によって破壊されました")
                }
                kill(car)
                car.passengers.forEach(Consumer { player: Player? -> warning.stop(player!!) })
                cancel()
            }
        }.runTaskTimer(PLVehicle.instance, 5L, 20L)
    }
}