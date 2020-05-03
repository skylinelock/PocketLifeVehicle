package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendPrefixedPluginMessage
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities.of
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

/**
 * @author sky_lock
 */
class SearchCommand : ICommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        val player = sender as Player
        if (!Permission.ADMIN_COMMAND.obtained(player) || args.size < 2) {
            val location = getVehicleLocation(player.uniqueId)
            if (location == null) {
                player.sendPrefixedPluginMessage(ChatColor.RED + "乗り物の現在地を取得できませんでした")
                return
            }
            if (location.world == player.location.world) {
                player.sendPrefixedPluginMessage(ChatColor.GREEN + "乗り物は現在(" + getLocationString(location) + ")にあります")
                return
            }
            player.sendPrefixedPluginMessage(ChatColor.GREEN + getLocationString(location))
            return
        }
        val name = args[1]
        val targetUUID = Bukkit.getPlayerUniqueId(name)
        if (targetUUID == null) {
            player.sendPrefixedPluginMessage(ChatColor.RED + "プレイヤーが見つかりませんでした")
            return
        }
        val location = getVehicleLocation(targetUUID)
        if (location == null) {
            player.sendPrefixedPluginMessage(ChatColor.RED + "プレイヤーの乗り物の現在地を取得できませんでした")
            return
        }
        player.sendPrefixedPluginMessage(ChatColor.GREEN + "(world=" + location.world.name + ", " + getLocationString(location) + ")")
    }

    private fun getVehicleLocation(target: UUID): Location? {
        val car = of(target) ?: return null
        return car.location
    }

    private fun getLocationString(loc: Location): String {
        return "x=" + loc.blockX.toString() + ", y=" + loc.blockY.toString() + ", z=" + loc.blockZ.toString()
    }
}