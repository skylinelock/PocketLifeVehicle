package dev.sky_lock.pocketlifevehicle.click

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.util.Profiles.getName
import dev.sky_lock.pocketlifevehicle.vehicle.Car
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.getCar
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.getOwner
import dev.sky_lock.pocketlifevehicle.vehicle.SeatArmorStand
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftArmorStand
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class CarClick(private val player: Player, private val seat: CraftArmorStand) {
    private val car: Car? = getCar(seat.handle as SeatArmorStand)
    fun accept() {
        if (seat.passengers.isNotEmpty()) {
            return
        }
        getOwner(car!!)?.let { owner ->
            val clicked = player.uniqueId
            val ownerName = getName(owner)
            if (player.isSneaking) {
                if (clicked != owner && !Permission.CAR_CLICK.obtained(player)) {
                    sendFailureInfo("この乗り物は $ownerName が所有しています")
                    return@let
                }
                car.openMenu(player)
                return@let
            }
            if (clicked == owner) {
                seat.addPassenger(player)
                return@let
            }
            if (car.status.isLocked) {
                sendFailureInfo("この乗り物には鍵が掛かっています")
                return@let
            }
            seat.addPassenger(player)
        }
    }

    private fun sendFailureInfo(message: String) {
        player.sendActionBar(ChatColor.YELLOW.toString() + "" + ChatColor.BOLD + "⚠⚠ " + ChatColor.RED + "" + ChatColor.BOLD + message + ChatColor.YELLOW + "" + ChatColor.BOLD + " ⚠⚠")
    }

}