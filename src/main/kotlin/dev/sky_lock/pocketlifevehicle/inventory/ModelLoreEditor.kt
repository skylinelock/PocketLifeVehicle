package dev.sky_lock.pocketlifevehicle.inventory

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.BlockPosition
import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.inventory.impl.InventoryModelOption
import dev.sky_lock.pocketlifevehicle.packet.BlockChangePacket
import dev.sky_lock.pocketlifevehicle.packet.OpenSignEditorPacket
import dev.sky_lock.pocketlifevehicle.packet.UpdateSignPacket
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

/**
 * @author sky_lock
 */
class ModelLoreEditor(private val player: Player, private val model: Model) {
    private val protocolManager = ProtocolLibrary.getProtocolManager()
    private val adapter = object : PacketAdapter(VehiclePlugin.instance, ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
        override fun onPacketReceiving(event: PacketEvent) {
            if (!editingPlayers.contains(event.player.uniqueId)) {
                return
            }
            event.isCancelled = true

            Bukkit.getScheduler().runTask(VehiclePlugin.instance) { _ ->
                model.lore = UpdateSignPacket(event.packet).lines
                    .filter { line -> line.isNotBlank() }
                player.openInventory(InventoryModelOption(player, model))
                close()
            }
        }
    }

    init {
        protocolManager.addPacketListener(adapter)
        open()
    }

    private fun close() {
        editingPlayers.remove(player.uniqueId)
        val blockChange = BlockChangePacket()
        val location = player.location.clone()
        location.y = 0.0
        blockChange.setLocation(location)
        blockChange.setBlockData(Material.BEDROCK)
        blockChange.send(player)

        protocolManager.removePacketListener(adapter)
    }

    private fun open() {
        val location = player.location.clone()
        location.y = 0.0
        val blockChange = BlockChangePacket()
        blockChange.setLocation(location)
        blockChange.setBlockData(Material.OAK_WALL_SIGN)
        blockChange.send(player)
        val packet = OpenSignEditorPacket()
        packet.setBlockPosition(BlockPosition(location.blockX, location.blockY, location.blockZ))
        packet.send(player)
        editingPlayers.remove(player.uniqueId)
        editingPlayers.add(player.uniqueId)
    }

    companion object {
        private val editingPlayers: MutableList<UUID> = ArrayList()
    }
}