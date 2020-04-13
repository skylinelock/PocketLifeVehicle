package dev.sky_lock.pocketlifevehicle.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.wrappers.BlockPosition
import com.comphenix.protocol.wrappers.WrappedBlockData
import org.bukkit.Location
import org.bukkit.Material

/**
 * @author sky_lock
 */
class BlockChangePacket : ServerPacket(PacketType.Play.Server.BLOCK_CHANGE) {
    fun setLocation(location: Location) {
        packet.blockPositionModifier.write(0, BlockPosition(location.blockX, location.blockY, location.blockZ))
    }

    fun setBlockData(type: Material) {
        packet.blockData.write(0, WrappedBlockData.createData(type))
    }
}