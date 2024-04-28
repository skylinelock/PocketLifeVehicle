package dev.sky_lock.pocketlifevehicle.packet

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

abstract class ServerPacket<T : PacketWrapper<T>>(private val wrapper: PacketWrapper<T>) {
    private val playerManager = PacketEvents.getAPI().playerManager

    fun send(player: Player) {
        playerManager.sendPacket(player, wrapper)
    }

    fun broadcast() {
        Bukkit.getOnlinePlayers().forEach { player -> send(player) }
    }
}