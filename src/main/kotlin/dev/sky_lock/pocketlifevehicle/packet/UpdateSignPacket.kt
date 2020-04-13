package dev.sky_lock.pocketlifevehicle.packet;

import com.comphenix.protocol.events.PacketContainer;

/**
 * @author sky_lock
 */

public class UpdateSignPacket extends ClientPacket {

    public UpdateSignPacket(PacketContainer packet) {
        super(packet);
    }

    public String[] getLines() {
        return getPacket().getStringArrays().read(0);
    }
}
