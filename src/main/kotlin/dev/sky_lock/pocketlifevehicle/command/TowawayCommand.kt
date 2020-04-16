package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.util.Profiles
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.of
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
        if (!Permission.ADMIN_COMMAND.obtained(sender) || args.size < 2) {
            if (towaway(player.uniqueId)) {
                player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "所有する車をアイテム化しました")
            } else {
                player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "車を所有していません")
            }
            return
        }
        val name = args[1]
        val targetUUID = Profiles.fetchUUID(name)
        if (targetUUID == null) {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "プレイヤーが見つかりませんでした")
            return
        }
        if (towaway(targetUUID)) {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "Player: " + name + " の車をアイテム化しました")
        } else {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "Player: " + name + " は車を所持していません")
        }
    }

    private fun towaway(uuid: UUID): Boolean {
        val carEntity = of(uuid) ?: return false
        tow(uuid)
        return true
    }
}