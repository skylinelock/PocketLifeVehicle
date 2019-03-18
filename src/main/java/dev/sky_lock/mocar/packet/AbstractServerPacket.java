package dev.sky_lock.mocar.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import dev.sky_lock.mocar.MoCar;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

/**
 * @author sky_lock
 */

public class AbstractServerPacket {
    private final MoCar plugin = MoCar.getInstance();
    private final PacketContainer packet;

    AbstractServerPacket(PacketType type) {
        this.packet = MoCar.getInstance().getProtocolManager().createPacket(type);
    }

    public void send(Player player) {
        try {
            plugin.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException ex) {
            plugin.getLogger().log(Level.WARNING, "Could not send a packet");
        }
    }

    PacketContainer getPacket() {
        return packet;
    }
}