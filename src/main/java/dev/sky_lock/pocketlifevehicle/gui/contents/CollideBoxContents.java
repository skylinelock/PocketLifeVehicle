package dev.sky_lock.pocketlifevehicle.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.pocketlifevehicle.gui.EditSessions;
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class CollideBoxContents extends MenuContents {
    private final Player player;
    private final ItemStack backItem = ItemStackBuilder.of(Material.ENDER_EYE, 1).name(ChatColor.RED + "戻る").build();
    private final ItemStack baseSideItem = ItemStackBuilder.of(Material.LIGHT_BLUE_CONCRETE, 1).name("横").build();
    private final ItemStack heightItem = ItemStackBuilder.of(Material.YELLOW_CONCRETE, 1).name("高さ").build();

    public CollideBoxContents(Player player) {
        this.player = player;
        this.addSlot(new Slot(4, backItem, event -> {
            InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.SETTING.ordinal()));
        }));
        this.addSlot(new Slot(20, baseSideItem, event -> {
            InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.COLLIDE_BASESIDE.ordinal()));
        }));
        this.addSlot(new Slot(24, heightItem, event -> {
            InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.COLLIDE_HEIGHT.ordinal()));
        }));
    }


    @Override
    public void onFlip(InventoryMenu inventoryMenu) {
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            if (session.getCollideBaseSide() != 0.0F) {
                ItemStack growBaseSide = ItemStackBuilder.of(baseSideItem).glow().lore(String.valueOf(session.getCollideBaseSide())).build();
                updateItemStack(20, growBaseSide);
            }
            if (session.getCollideHeight() != 0.0F) {
                ItemStack growHeight = ItemStackBuilder.of(heightItem).glow().lore(String.valueOf(session.getCollideHeight())).build();
                updateItemStack(24, growHeight);
            }
            inventoryMenu.update();
        });
    }
}
