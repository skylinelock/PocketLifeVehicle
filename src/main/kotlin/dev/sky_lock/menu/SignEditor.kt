package dev.sky_lock.menu

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.BlockPosition
import dev.sky_lock.pocketlifevehicle.packet.BlockChangePacket
import dev.sky_lock.pocketlifevehicle.packet.OpenSignEditorPacket
import dev.sky_lock.pocketlifevehicle.packet.UpdateSignPacket
import net.md_5.bungee.api.chat.ClickEvent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.function.Consumer

/**
 * @author sky_lock
 */
open class SignEditor(plugin: JavaPlugin, private val player: Player, todo: Consumer<UpdateSignPacket>) {
    fun close() {
        opening.remove(player.uniqueId)
        val blockChange = BlockChangePacket()
        val location = player.location.clone()
        location.y = 0.0
        blockChange.setLocation(location)
        blockChange.setBlockData(Material.BEDROCK)
        blockChange.send(player)
    }

    fun open() {
        val blockChange = BlockChangePacket()
        val location = player.location.clone()
        location.y = 0.0
        blockChange.setLocation(location)
        blockChange.setBlockData(Material.OAK_WALL_SIGN)
        blockChange.send(player)
        val packet = OpenSignEditorPacket()
        packet.setBlockPosition(BlockPosition(location.blockX, location.blockY, location.blockZ))
        packet.send(player)
        opening.remove(player.uniqueId)
        opening.add(player.uniqueId)
    }

    companion object {
        private val opening: MutableList<UUID> = ArrayList()
    }

    init {
        val adapter: PacketAdapter = object : PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
            override fun onPacketReceiving(event: PacketEvent) {
                if (!opening.contains(event.player.uniqueId)) {
                    return
                }
                val player = event.player
                event.isCancelled = true
                close()
                todo.accept(UpdateSignPacket(event.packet))
            }
        }
        ProtocolLibrary.getProtocolManager().addPacketListener(adapter)
    }
}