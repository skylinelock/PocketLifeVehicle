package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.util.Profiles
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.of
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
                player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "車を所有していません")
            } else {
                sendLocation(player, location)
            }
            return
        }
        val name = args[1]
        val targetUUID = Profiles.fetchUUID(name)
        if (targetUUID == null) {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "プレイヤーが見つかりませんでした")
            return
        }
        val location = getCarLocation(targetUUID)
        if (location == null) {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "指定したプレイヤーは車を所有していません")
        } else {
            sendLocation(player, location)
        }
    }

    private fun getCarLocation(target: UUID): Location? {
        val car = of(target) ?: return null
        return car.location
    }

    private fun sendLocation(player: Player, loc: Location) {
        player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "-------------------------------------------")
        player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "World : " + loc.world.name)
        player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "X : " + loc.blockX)
        player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "Y : " + loc.blockY)
        player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "Z : " + loc.blockZ)
        player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "-------------------------------------------")
    }
}