package dev.sky_lock.pocketlifevehicle.packet

import com.comphenix.protocol.PacketType

/**
 * @author sky_lock
 */
class FakeExplosionPacket : ServerPacket(PacketType.Play.Server.EXPLOSION) {
    fun setX(value: Double) {
        packet.doubles.write(0, value)
    }

    fun setY(value: Double) {
        packet.doubles.write(1, value)
    }

    fun setZ(value: Double) {
        packet.doubles.write(2, value)
    }

    fun setRadius(value: Float) {
        packet.float.write(0, value)
    }
}