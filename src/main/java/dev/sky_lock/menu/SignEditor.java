package dev.sky_lock.menu;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import dev.sky_lock.packet.BlockChangePacket;
import dev.sky_lock.packet.OpenSignEditorPacket;
import dev.sky_lock.packet.UpdateSignPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author sky_lock
 */

public class SignEditor {
    private final static List<UUID> opening = new ArrayList<>();
    private final Player player;

    public SignEditor(JavaPlugin plugin, Player player, Consumer<UpdateSignPacket> todo) {
        this.player = player;
        PacketAdapter adapter = new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (!opening.contains(event.getPlayer().getUniqueId())) {
                    return;
                }
                Player player = event.getPlayer();
                event.setCancelled(true);
                close();
                todo.accept(new UpdateSignPacket(event.getPacket()));
            }
        };
        ProtocolLibrary.getProtocolManager().addPacketListener(adapter);
    }

    public void close() {
        opening.remove(player.getUniqueId());
        BlockChangePacket blockChange = new BlockChangePacket();
        Location location = player.getLocation().clone();
        location.setY(0);
        blockChange.setLocation(location);
        blockChange.setBlockData(Bukkit.getServer().createBlockData(Material.BEDROCK));
        blockChange.send(player);

    }

    public void open() {
        BlockChangePacket blockChange = new BlockChangePacket();
        Location location = player.getLocation().clone();
        location.setY(0);
        blockChange.setLocation(location);
        blockChange.setBlockData(Bukkit.getServer().createBlockData(Material.WALL_SIGN));
        blockChange.send(player);

        OpenSignEditorPacket packet = new OpenSignEditorPacket();
        packet.setBlockPosition(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        packet.send(player);

        opening.remove(player.getUniqueId());
        opening.add(player.getUniqueId());
    }

}
