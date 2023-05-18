package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.text.ext.sendVehiclePrefixedErrorMessage
import dev.sky_lock.pocketlifevehicle.text.ext.sendVehiclePrefixedRawMessage
import dev.sky_lock.pocketlifevehicle.text.ext.sendVehiclePrefixedSuccessMessage
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */
class ReloadCommand : ICommand, IAdminCommand, IConsoleCommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        if (args.size < 2) {
            sender.sendVehiclePrefixedErrorMessage( "引数が足りません")
            return
        }
        val flag = args[1]
        when {
            flag.equals("from", ignoreCase = true) -> {
                ModelRegistry.reloadConfig()
                VehiclePlugin.instance.pluginConfiguration.load()
                VehiclePlugin.instance.parkingViolationList.load()
                sender.sendVehiclePrefixedSuccessMessage( "ディスクからデータを読み込みました")
            }
            flag.equals("to", ignoreCase = true) -> {
                VehiclePlugin.instance.pluginConfiguration.save()
                VehiclePlugin.instance.parkingViolationList.save()
                ModelRegistry.saveToFile()
                sender.sendVehiclePrefixedSuccessMessage( "ディスクへデータを保存しました")
            }
            else -> {
                sender.sendVehiclePrefixedRawMessage("/vehicle reload [from/to]")
            }
        }
    }
}