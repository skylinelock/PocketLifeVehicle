package dev.sky_lock.pocketlifevehicle.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.wrappers.BlockPosition

/**
 * @author sky_lock
 */
class OpenSignEditorPacket : ServerPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR) {
    fun setBlockPosition(position: BlockPosition) {
        packet.blockPositionModifier.write(0, position)
    }
}