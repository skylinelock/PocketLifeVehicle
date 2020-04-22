package dev.sky_lock.pocketlifevehicle.click

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.util.Profiles.getName
import dev.sky_lock.pocketlifevehicle.vehicle.Vehicle
import dev.sky_lock.pocketlifevehicle.vehicle.ModelArmorStand
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities.getCar
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities.getOwner
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
        val vehicle: Vehicle
        if (handle is SeatArmorStand) {
            if (armorStand.passengers.isNotEmpty()) {
                return
            }
            vehicle = getCar(handle) ?: return
            val owner = getOwner(vehicle) ?: return
            val ownerName = getName(owner)

            if (player.isSneaking) {
                if (clicked != owner && !Permission.VEHICLE_OPEN_GUI.obtained(player)) {
                    sendFailureInfo("この乗り物は $ownerName が所有しています")
                    return
                }
                vehicle.openMenu(player)
                return
            }

            if (clicked == owner) {
                armorStand.addPassenger(player)
                return
            }
            if (vehicle.status.isLocked) {
                sendFailureInfo("この乗り物には鍵が掛かっています")
                return
            }
            armorStand.addPassenger(player)
        } else if (handle is ModelArmorStand) {
            vehicle = getCar(handle) ?: return
            val owner = getOwner(vehicle) ?: return
            val ownerName = getName(owner)

            if (player.isSneaking) {
                if (clicked != owner && !Permission.VEHICLE_OPEN_GUI.obtained(player)) {
                    sendFailureInfo("この乗り物は $ownerName が所有しています")
                    return
                }
                vehicle.openMenu(player)
                return
            }
            if (vehicle.passengers.size >= vehicle.model.spec.capacity.value()) {
                sendFailureInfo("この乗り物は満員です")
                return
            }
            if (clicked == owner) {
                vehicle.addPassenger(player)
                return
            }
            if (vehicle.status.isLocked) {
                sendFailureInfo("この乗り物には鍵が掛かっています")
                return
            }
            vehicle.addPassenger(player)
        }

    }

    private fun sendFailureInfo(message: String) {
        player.sendActionBar(ChatColor.YELLOW + ChatColor.BOLD + "⚠⚠ " + ChatColor.RED + "" + ChatColor.BOLD + message + ChatColor.YELLOW + "" + ChatColor.BOLD + " ⚠⚠")
    }

}