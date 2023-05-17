package dev.sky_lock.pocketlifevehicle.packet

import com.comphenix.protocol.PacketType

/**
 * @author sky_lock
 */
class AnimationPacket : ServerPacket(PacketType.Play.Server.ANIMATION) {
    fun setEntityID(value: Int) {
        packet.integers.write(0, value)
    }

    fun setAnimationType(type: AnimationType) {
        packet.integers.write(1, type.ordinal)
    }

    enum class AnimationType {
        SWING_MAIN_ARM, SWING_OFFHAND
    }
}