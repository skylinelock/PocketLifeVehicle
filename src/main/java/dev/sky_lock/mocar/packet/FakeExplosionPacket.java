package dev.sky_lock.mocar.packet;

import com.comphenix.protocol.PacketType;
import dev.sky_lock.glassy.packet.ServerPacket;

/**
 * @author sky_lock
 */

public class FakeExplosionPacket extends ServerPacket {

    public FakeExplosionPacket() {
        super(PacketType.Play.Server.EXPLOSION);
    }

    public void setX(double value) {
        getPacket().getDoubles().write(0, value);
    }

    public void setY(double value) {
        getPacket().getDoubles().write(1, value);
    }

    public void setZ(double value) {
        getPacket().getDoubles().write(2, value);
    }

    public void setRadius(float value) {
        getPacket().getFloat().write(0, value);
    }

}
