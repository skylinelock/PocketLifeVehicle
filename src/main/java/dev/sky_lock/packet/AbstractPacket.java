package dev.sky_lock.packet;

import com.comphenix.protocol.events.PacketContainer;

/**
 * @author sky_lock
 */

public class AbstractPacket {
    private final PacketContainer packet;

    public AbstractPacket(PacketContainer packet) {
        this.packet = packet;
    }

    protected PacketContainer getPacket() {
        return packet;
    }
}
