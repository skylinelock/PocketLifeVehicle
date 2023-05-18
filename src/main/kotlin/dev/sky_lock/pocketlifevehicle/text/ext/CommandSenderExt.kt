package dev.sky_lock.pocketlifevehicle.text.ext

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.text.Line
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */

fun CommandSender.sendVehicleText(line: Line) {
    val prefix = Line().darkGray("[").gray("Vehicle").darkGray("] ")
    if (Permission.ADMIN_COMMAND.obtained(this)) {
        this.sendMessage(prefix.connect(line).toComponent())
    } else {
        this.sendMessage(line.toComponent())
    }
}

fun CommandSender.sendVehiclePrefixedRawMessage(text: String) {
    sendVehicleText(Line().raw(text))
}

fun CommandSender.sendVehiclePrefixedSuccessMessage(text: String) {
    sendVehicleText(Line().green(text))
}

fun CommandSender.sendVehiclePrefixedErrorMessage(text: String) {
    sendVehicleText(Line().red(text))
}