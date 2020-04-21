package dev.sky_lock.pocketlifevehicle.click

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.util.Profiles.getName
import dev.sky_lock.pocketlifevehicle.vehicle.Car
import dev.sky_lock.pocketlifevehicle.vehicle.CarArmorStand
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.getCar
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.getOwner
import dev.sky_lock.pocketlifevehicle.vehicle.SeatArmorStand
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftArmorStand
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class CarClick(private val player: Player, private val armorStand: CraftArmorStand) {
    fun accept() {
        val handle = armorStand.handle
        val clicked = player.uniqueId
        val car: Car
        if (handle is SeatArmorStand) {
            if (armorStand.passengers.isNotEmpty()) {
                return
            }
            car = getCar(handle) ?: return
            val owner = getOwner(car) ?: return
            val ownerName = getName(owner)

            if (player.isSneaking) {
                if (clicked != owner && !Permission.VEHICLE_OPEN_GUI.obtained(player)) {
                    sendFailureInfo("この乗り物は $ownerName が所有しています")
                    return
                }
                car.openMenu(player)
                return
            }

            if (clicked == owner) {
                armorStand.addPassenger(player)
                return
            }
            if (car.status.isLocked) {
                sendFailureInfo("この乗り物には鍵が掛かっています")
                return
            }
            armorStand.addPassenger(player)
        } else if (handle is CarArmorStand) {
            car = getCar(handle) ?: return
            val owner = getOwner(car) ?: return
            val ownerName = getName(owner)

            if (player.isSneaking) {
                if (clicked != owner && !Permission.VEHICLE_OPEN_GUI.obtained(player)) {
                    sendFailureInfo("この乗り物は $ownerName が所有しています")
                    return
                }
                car.openMenu(player)
                return
            }
            if (car.passengers.size >= car.model.spec.capacity.value()) {
                sendFailureInfo("この乗り物は満員です")
                return
            }
            if (clicked == owner) {
                car.addPassenger(player)
                return
            }
            if (car.status.isLocked) {
                sendFailureInfo("この乗り物には鍵が掛かっています")
                return
            }
            car.addPassenger(player)
        }

    }

    private fun sendFailureInfo(message: String) {
        player.sendActionBar(ChatColor.YELLOW.toString() + "" + ChatColor.BOLD + "⚠⚠ " + ChatColor.RED + "" + ChatColor.BOLD + message + ChatColor.YELLOW + "" + ChatColor.BOLD + " ⚠⚠")
    }

}