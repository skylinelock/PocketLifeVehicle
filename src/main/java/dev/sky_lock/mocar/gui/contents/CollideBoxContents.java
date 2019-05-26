package dev.sky_lock.mocar.gui.contents;

import com.google.common.collect.ImmutableList;
import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.ModelMenuIndex;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class CollideBoxContents extends MenuContents {
    private final Player player;
    private final ItemStack baseSideItem = ItemStackBuilder.of(Material.LIGHT_BLUE_CONCRETE, 1).name("BaseSide").build();
    private final ItemStack heightItem = ItemStackBuilder.of(Material.YELLOW_CONCRETE, 1).name("Height").build();

    public CollideBoxContents(Player player) {
        this.player = player;
        this.addSlot(new Slot(20, baseSideItem, event -> {
            InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.COLLIDE_BASESIDE.value()));
        }));
        this.addSlot(new Slot(24, heightItem, event -> {
            InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.COLLIDE_HEIGHT.value()));
        }));
        //this.addSlot(new Slot());
    }


    @Override
    public void onFlip(InventoryMenu inventoryMenu) {
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            if (session.getCollideBaseSide() != 0.0F && session.getCollideHeight() != 0.0F) {
                inventoryMenu.flip(player, ModelMenuIndex.SETTING.value());
                return;
            }
            if (session.getCollideBaseSide() != 0.0F) {
                ItemStack growBaseSide = new ItemStackBuilder(baseSideItem).grow().lore(ImmutableList.of(String.valueOf(session.getCollideBaseSide()))).build();
                updateItemStack(20, growBaseSide);
            }
            if (session.getCollideHeight() != 0.0F) {
                ItemStack growHeight = new ItemStackBuilder(heightItem).grow().lore(ImmutableList.of(String.valueOf(session.getCollideHeight()))).build();
                updateItemStack(24, growHeight);
            }
            inventoryMenu.update();
        });
    }
}
