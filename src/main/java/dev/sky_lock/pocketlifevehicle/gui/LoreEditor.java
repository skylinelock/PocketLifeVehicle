package dev.sky_lock.pocketlifevehicle.gui;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.SignEditor;
import dev.sky_lock.pocketlifevehicle.PLVehicle;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class LoreEditor extends SignEditor {

    public LoreEditor(Player player) {
        super(PLVehicle.getInstance(), player, (packet) -> {
            List<String> lore = Arrays.stream(packet.getLines()).filter(line -> line.length() > 0).map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());
            EditSessions.of(player.getUniqueId()).ifPresent(session -> {
                session.setLore(lore);
            });
            new BukkitRunnable() {
                @Override
                public void run() {
                    InventoryMenu inventoryMenu = (InventoryMenu) player.getOpenInventory().getTopInventory().getHolder();
                    inventoryMenu.open(player, ModelMenuIndex.SETTING.value());
                }
            }.runTaskLater(PLVehicle.getInstance(), 1L);
        });
    }
}
