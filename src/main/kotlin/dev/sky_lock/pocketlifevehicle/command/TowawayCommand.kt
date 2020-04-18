package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.extension.plus
import dev.sky_lock.pocketlifevehicle.extension.sendPrefixedPluginMessage
import dev.sky_lock.pocketlifevehicle.util.Profiles
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.tow
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

/**
 * @author sky_lock
 */
class TowawayCommand : ICommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        val player = sender as Player
        if (!Permission.ADMIN_COMMAND.obtained(sender)) {
            if (towaway(player.uniqueId)) {
                player.sendPrefixedPluginMessage(ChatColor.GREEN + "乗り物をアイテム化しました")
            } else {
                player.sendPrefixedPluginMessage(ChatColor.RED + "乗り物をアイテム化できませんでした")
            }
            return
        }
        val name = args[1]
        val targetUUID = Profiles.fetchUUID(name)
        if (targetUUID == null) {
            player.sendPrefixedPluginMessage(ChatColor.GREEN + "プレイヤーが見つかりませんでした")
            return
        }
        if (towaway(targetUUID)) {
            player.sendPrefixedPluginMessage(ChatColor.GREEN + name + " の乗り物をアイテム化しました")
        } else {
            player.sendPrefixedPluginMessage(ChatColor.RED + name + " の乗り物をアイテム化できませんでした")
        }
    }

    private fun towaway(uuid: UUID): Boolean {
        CarEntities.of(uuid) ?: return false
        tow(uuid)
        return true
    }
}