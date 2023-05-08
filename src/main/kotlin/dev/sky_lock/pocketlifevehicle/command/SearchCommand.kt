package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedErrorMessage
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedSuccessMessage
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class SearchCommand : ICommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        val player = sender as Player
        if (!Permission.ADMIN_COMMAND.obtained(player) || args.size < 2) {
            val location = VehicleManager.getLocation(player.uniqueId)
            if (location == null) {
                player.sendVehiclePrefixedErrorMessage( "乗り物の現在地を取得できませんでした")
                return
            }
            if (location.world == player.location.world) {
                player.sendVehiclePrefixedSuccessMessage( "乗り物は現在(" + getLocationString(location) + ")にあります")
                return
            }
            player.sendVehiclePrefixedErrorMessage( "別ワールドにある乗り物の現在地は取得できません")
            return
        }
        val name = args[1]
        val targetUUID = Bukkit.getPlayerUniqueId(name)
        if (targetUUID == null) {
            player.sendVehiclePrefixedErrorMessage( "プレイヤーが見つかりませんでした")
            return
        }
        val location = VehicleManager.getLocation(targetUUID)
        if (location == null) {
            player.sendVehiclePrefixedErrorMessage( "プレイヤーの乗り物の現在地を取得できませんでした")
            return
        }
        player.sendVehiclePrefixedSuccessMessage( "(world=" + location.world.name + ", " + getLocationString(location) + ")")
    }

    private fun getLocationString(loc: Location): String {
        return "x=" + loc.blockX.toString() + ", y=" + loc.blockY.toString() + ", z=" + loc.blockZ.toString()
    }
}