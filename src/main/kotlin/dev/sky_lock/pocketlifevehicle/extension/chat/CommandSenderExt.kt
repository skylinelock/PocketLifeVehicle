package dev.sky_lock.pocketlifevehicle.extension.chat

import dev.sky_lock.pocketlifevehicle.Permission
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */

fun CommandSender.sendVehicleTextComponent(component: Component) {
    val prefix = Line().darkGray("[").gray("Vehicle").darkGray("] ")
    if (Permission.ADMIN_COMMAND.obtained(this)) {
        this.sendMessage(prefix.toComponent().append(component))
    } else {
        this.sendMessage(component)
    }
}

fun CommandSender.sendVehiclePrefixedRawMessage(text: String) {
    sendVehicleTextComponent(Component.text(text))
}

fun CommandSender.sendVehiclePrefixedSuccessMessage(text: String) {
    sendVehicleTextComponent(Component.text(text, NamedTextColor.GREEN))
}

fun CommandSender.sendVehiclePrefixedErrorMessage(text: String) {
    sendVehicleTextComponent(Component.text(text, NamedTextColor.RED))
}

fun CommandSender.sendRacePrefixedRawMessage(text: String) {
    this.sendMessage(
            Line().darkGray("[").gray("Race").darkGray("] ").toComponent().append(Component.text(text))
    )
}