package dev.sky_lock.pocketlifevehicle.inventory

import dev.sky_lock.pocketlifevehicle.packet.BlockChange
import dev.sky_lock.pocketlifevehicle.packet.OpenSignEditor
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

/**
 * @author sky_lock
 */
class ModelLoreEditor(private val player: Player, val model: Model) {
    init {
        open()
    }

    fun close() {
        editMap.remove(player.uniqueId)
        val location = player.location.clone()
        val blockChange = BlockChange(location.blockX, 0, location.blockZ, Material.BEDROCK)
        blockChange.send(player)
    }

    private fun open() {
        val location = player.location.clone()
        val blockChange = BlockChange(location.blockX, 0, location.blockZ, Material.OAK_WALL_SIGN)
        blockChange.send(player)
        OpenSignEditor(location.blockX, location.blockY, location.blockZ).send(player)
        editMap.remove(player.uniqueId)
        editMap[player.uniqueId] = this
    }

    companion object {
        val editMap : MutableMap<UUID, ModelLoreEditor> = HashMap()
    }
}