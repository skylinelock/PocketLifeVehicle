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
            val location = getCarLocation(player.uniqueId)
            if (location == null) {
                player.sendPrefixedPluginMessage(ChatColor.RED + "乗り物の現在地を取得できませんでした")
            } else {
                player.sendPrefixedPluginMessage(ChatColor.GREEN + getLocationString(location))
            }
            return
        }
        val name = args[1]
        val targetUUID = Bukkit.getPlayerUniqueId(name)
        if (targetUUID == null) {
            player.sendPrefixedPluginMessage(ChatColor.RED + "プレイヤーが見つかりませんでした")
            return
        }
        val location = getCarLocation(targetUUID)
        if (location == null) {
            player.sendPrefixedPluginMessage(ChatColor.RED + "プレイヤーの乗り物の現在地を取得できませんでした")
        } else {
            player.sendPrefixedPluginMessage(ChatColor.GREEN + getLocationString(location))
        }
    }

    private fun getCarLocation(target: UUID): Location? {
        val car = of(target) ?: return null
        return car.location
    }

    private fun getLocationString(loc: Location): String {
        return "World: " + loc.world.name + ", x: " + loc.blockX.toString() + ", y: " + loc.blockY.toString() + ", z: " + loc.blockZ.toString()
    }
}