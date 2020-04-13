package dev.sky_lock.pocketlifevehicle.packet

import com.comphenix.protocol.events.PacketContainer

/**
 * @author sky_lock
 */
class UpdateSignPacket(packet: PacketContainer) : ClientPacket(packet) {
    val lines: Array<String>
        get() = packet.stringArrays.read(0)
}