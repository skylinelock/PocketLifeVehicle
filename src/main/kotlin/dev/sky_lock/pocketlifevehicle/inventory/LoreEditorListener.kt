package dev.sky_lock.pocketlifevehicle.inventory

import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign
import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.inventory.impl.InventoryModelOption
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class LoreEditorListener: SimplePacketListenerAbstract(com.github.retrooper.packetevents.event.PacketListenerPriority.NORMAL) {
    override fun onPacketPlayReceive(event: PacketPlayReceiveEvent) {
        super.onPacketPlayReceive(event)
        if (event.packetType != PacketType.Play.Client.UPDATE_SIGN) return
        val player = event.player as Player
        if (!ModelLoreEditor.editMap.contains(player.uniqueId)) return

        val editor = ModelLoreEditor.editMap[player.uniqueId] ?: return
        event.isCancelled = true

        Bukkit.getScheduler().runTask(VehiclePlugin.instance) { _ ->
            val wrapper = WrapperPlayClientUpdateSign(event)
            wrapper.textLines.filter { line -> line.isNotBlank()}
            player.openInventory(InventoryModelOption(player, editor.model))
            editor.close()
        }
    }
}