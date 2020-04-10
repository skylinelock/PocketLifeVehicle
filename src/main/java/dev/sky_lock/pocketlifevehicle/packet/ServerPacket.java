package dev.sky_lock.pocketlifevehicle.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

/**
 * @author sky_lock
 */

public class ServerPacket extends AbstractPacket {

    ServerPacket(PacketType type) {
        super(ProtocolLibrary.getProtocolManager().createPacket(type));
    }

    public void send(Player player) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, getPacket());
        } catch (InvocationTargetException ex) {
            Bukkit.getLogger().log(Level.WARNING, "Could not send a packet");
        }
    }

    public void broadCast() {
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(getPacket());
    }
}
