package dev.sky_lock.pocketlifevehicle.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.lang.reflect.InvocationTargetException
import java.util.logging.Level

/**
 * @author sky_lock
 */
open class ServerPacket constructor(val type: PacketType)
    : AbstractPacket(ProtocolLibrary.getProtocolManager().createPacket(type)) {

    fun send(player: Player) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet)
        } catch (ex: InvocationTargetException) {
            Bukkit.getLogger().log(Level.WARNING, "Could not send a packet")
        }
    }

    fun broadCast() {
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet)
    }
}