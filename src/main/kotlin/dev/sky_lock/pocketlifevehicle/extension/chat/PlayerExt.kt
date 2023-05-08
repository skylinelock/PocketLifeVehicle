package dev.sky_lock.pocketlifevehicle.extension.chat

import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
 
fun Player.sendActionBar(line: Line) {
    this.sendActionBar(line.toComponent())
}

fun Player.sendMessage(line: Line) {
    this.sendMessage(line.toComponent())
}