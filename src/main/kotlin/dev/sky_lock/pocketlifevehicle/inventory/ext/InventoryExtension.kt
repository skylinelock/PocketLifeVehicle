package dev.sky_lock.pocketlifevehicle.inventory.ext

import dev.sky_lock.pocketlifevehicle.inventory.ModelLoreEditor
import dev.sky_lock.pocketlifevehicle.inventory.impl.ContainerModelTextEdit
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

fun Player.openModelLoreEditor(model: Model) {
    ModelLoreEditor(this, model)
}

fun Player.openModelTextEditor(title: String, default: String, type: ContainerModelTextEdit.ModifyType, model: Model?) {
    openInventory(ContainerModelTextEdit(title, default, type, model, this).bukkitView)
}