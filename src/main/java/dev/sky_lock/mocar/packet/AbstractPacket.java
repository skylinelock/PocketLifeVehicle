package dev.sky_lock.mocar.packet;

import com.comphenix.protocol.events.PacketContainer;

/**
 * @author sky_lock
 */

public class AbstractPacket {
    private final PacketContainer packet;

    AbstractPacket(PacketContainer packet) {
        this.packet = packet;
    }

    public PacketContainer getPacket() {
        return packet;
    }
}
