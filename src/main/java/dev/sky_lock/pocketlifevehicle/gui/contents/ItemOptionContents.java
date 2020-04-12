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

public class ItemOptionContents extends MenuContents {
    private final Player player;
    private final ItemStack backItem = ItemStackBuilder.of(Material.ENDER_EYE, 1).name(ChatColor.RED + "戻る").build();
    private ItemStack itemItem = ItemStackBuilder.of(Material.MAGMA_CREAM, 1).name("アイテム").build();
    private ItemStack positionItem = ItemStackBuilder.of(Material.SLIME_BALL, 1).name("アイテム位置").build();

    public ItemOptionContents(Player player) {
        this.player = player;
        this.addSlot(new Slot(4, backItem, event -> {
            InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.SETTING.ordinal()));
        }));
        this.addSlot(new Slot(21, itemItem, event -> {
            InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.ITEM_SELECT.ordinal()));
        }));
        this.addSlot(new Slot(23, positionItem, event -> {
            InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.ITEM_POS.ordinal()));
        }));
    }

    @Override
    public void onFlip(InventoryMenu menu) {
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            if (session.isItemValid()) {
                itemItem = ItemStackBuilder.of(itemItem).grow().lore(session.getItemType().name(), String.valueOf(session.getItemId())).build();
                updateItemStack(21, itemItem);
            }
            if (session.getPosition() != null) {
                positionItem = ItemStackBuilder.of(positionItem).grow().lore(session.getPosition().getLabel()).build();
                updateItemStack(23, positionItem);
            }
            menu.update();
        });
    }
}
