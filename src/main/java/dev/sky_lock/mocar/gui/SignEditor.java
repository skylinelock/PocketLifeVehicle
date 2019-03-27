package dev.sky_lock.mocar.gui;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.packet.BlockChangePacket;
import dev.sky_lock.mocar.packet.OpenSignEditorPacket;
import dev.sky_lock.mocar.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * @author sky_lock
 */

public class SignEditor {
    private final static List<UUID> opening = new ArrayList<>();

    public static void close(Player player) {
        opening.remove(player.getUniqueId());
        BlockChangePacket blockChange = new BlockChangePacket();
        blockChange.setLocation(player.getLocation());
        blockChange.setBlockData(Bukkit.getServer().createBlockData(Material.AIR));
        blockChange.send(player);
    }

    public static void registerListener() {
        PacketAdapter adapter = new PacketAdapter(MoCar.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (!opening.contains(event.getPlayer().getUniqueId())) {
                    return;
                }
                List<String> lores = new ArrayList<>();
                IntStream.range(0, 4).forEachOrdered(index -> lores.add(MessageUtil.attachColor(event.getPacket().getStringArrays().read(0)[index])));
                lores.removeAll(Arrays.asList("", null));
                EditSessions.get(event.getPlayer().getUniqueId()).ifPresent(session -> {
                    session.setLore(lores);
                });
                Bukkit.getScheduler().runTaskLater(MoCar.getInstance(), () -> {
                    new ModelSettingMenu(event.getPlayer()).open(event.getPlayer());
                }, 1L);
                event.setCancelled(true);
                opening.remove(event.getPlayer().getUniqueId());
            }
        };
        MoCar.getInstance().getProtocolManager().addPacketListener(adapter);
    }

    public void open(Player player) {
        BlockChangePacket blockChange = new BlockChangePacket();
        blockChange.setLocation(player.getLocation());
        blockChange.setBlockData(Bukkit.getServer().createBlockData(Material.WALL_SIGN));
        blockChange.send(player);

        OpenSignEditorPacket packet = new OpenSignEditorPacket();
        packet.setBlockPosition(new BlockPosition(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
        packet.send(player);

        opening.remove(player.getUniqueId());
        opening.add(player.getUniqueId());
    }

}
